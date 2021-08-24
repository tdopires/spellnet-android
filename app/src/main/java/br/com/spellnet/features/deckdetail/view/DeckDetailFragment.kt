package br.com.spellnet.features.deckdetail.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.hide
import br.com.spellnet.commom.safeLet
import br.com.spellnet.commom.show
import br.com.spellnet.databinding.DeckDetailFragmentBinding
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import br.com.spellnet.entity.Deck
import br.com.spellnet.features.deckdetail.viewmodel.DeckDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeckDetailFragment : Fragment() {

    companion object {
        private const val KEY_ARGS_DECK = "KEY_ARGS_DECK"

        fun newInstance(deck: Deck) = DeckDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ARGS_DECK, deck)
            }
        }
    }

    private val deckDetailsViewModel: DeckDetailViewModel by viewModel()

    private lateinit var binding: DeckDetailFragmentBinding

    private var deckDetailAdapter: DeckDetailAdapter? = null

    private val deck by lazy { this.arguments?.getParcelable(KEY_ARGS_DECK) as Deck? }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckDetailFragmentBinding.inflate(inflater, container, false)
        deck?.let {
            bindViewComponents(it)
            bindToViewModel()
        } ?: run {
            fragmentManager?.popBackStackImmediate()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deck?.let {
            deckDetailsViewModel.openDeck(it)
        }
    }

    private fun bindViewComponents(deck: Deck) {
        deckDetailAdapter = DeckDetailAdapter(deck).apply {
            onDeckImportUrlClickListener = { deckImportUrl ->
                val uri = Uri.parse(deckImportUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            onCardPreviewClickListener = { card ->
                val uri = Uri.parse("https://ligamagic.com.br/?view=cards/card&card=${card.name}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            onCardPricingRetryClickListener = { card ->
                deckDetailsViewModel.retryFetchCardPricing(card)
                    .observe(viewLifecycleOwner, Observer { resource ->
                        handleCardPricingResource(card, resource)
                    })
            }
            onHaveCardQuantityChangedListener = { cardQuantity ->
                deckDetailsViewModel.updateCardCollection(cardQuantity)
                updateCardHaveQuantity(cardQuantity)
            }
        }
        binding.cardList.adapter = deckDetailAdapter

        showBackButtonOnToolbar()
        setHasOptionsMenu(true)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    hideBackButtonOnToolbar()
                    fragmentManager?.popBackStack()
                    if (isEnabled) {
                        isEnabled = false
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            hideBackButtonOnToolbar()
            fragmentManager?.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBackButtonOnToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun hideBackButtonOnToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun bindToViewModel() {
        deckDetailsViewModel.deck().observe(viewLifecycleOwner, Observer { deckResource ->
            when (deckResource) {
                is Resource.Loading -> {
                    binding.loading.show()
                    binding.cardList.hide()
                }
                is Resource.Success -> {
                    binding.loading.hide()
                    binding.cardList.show()
                    deckResource.data.map { cardEntry ->
                        val (haveCardQuantity, resourceCardPricing) = cardEntry.value
                        resourceCardPricing.observe(viewLifecycleOwner, Observer { resource ->
                            handleCardPricingResource(cardEntry.key, resource)
                        })
                        haveCardQuantity.observe(viewLifecycleOwner, Observer {
                            if (it is Resource.Success) {
                                deckDetailAdapter?.updateCardHaveQuantity(it.data)
                            }
                        })
                    }
                }
            }
        })
    }

    private fun handleCardPricingResource(card: Card, resource: Resource<CardPricing>?) {
        safeLet(deckDetailAdapter, resource) { adapter, safeResource ->
            adapter.updateCardPricing(card, safeResource)
        }
    }

}
