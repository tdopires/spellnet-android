package br.com.spellnet.features.deckdetail.view

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.safeLet
import br.com.spellnet.databinding.DeckDetailFragmentBinding
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import br.com.spellnet.entity.Deck
import br.com.spellnet.features.deckdetail.viewmodel.DeckDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DeckDetailFragmentBinding.inflate(inflater, container, false)
        deck?.let {
            bindViewComponents(it)
            bindToViewModel(it)
        } ?: run {
            fragmentManager?.popBackStackImmediate()
        }
        return binding.root
    }

    private fun bindViewComponents(deck: Deck) {
        deckDetailAdapter = DeckDetailAdapter(deck).apply {
            onCardPricingRetryClickListener = { card ->
                deckDetailsViewModel.retryFetchCardPricing(card)
                    .observe(this@DeckDetailFragment, Observer { resource ->
                        handleCardPricingResource(card, resource)
                    })
            }
            onHaveCardQuantityChangedListener = { cardQuantity ->
                deckDetailsViewModel.updateCardCollection(cardQuantity)
                updateCardHaveQuantity(cardQuantity)
            }
        }
        binding.cardList.adapter = deckDetailAdapter
    }

    private fun bindToViewModel(deck: Deck) {
        deckDetailsViewModel.openDeck(deck).map { cardEntry ->
            val (haveCardQuantity, resourceCardPricing) = cardEntry.value
            resourceCardPricing.observe(this, Observer { resource ->
                handleCardPricingResource(cardEntry.key, resource)
            })
            haveCardQuantity?.let {
                deckDetailAdapter?.updateCardHaveQuantity(it)
            }
        }
    }

    private fun handleCardPricingResource(card: Card, resource: Resource<CardPricing>?) {
        safeLet(deckDetailAdapter, resource) { adapter, safeResource ->
            adapter.updateCardPricing(card, safeResource)
        }
    }

}
