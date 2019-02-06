package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.safeLet
import br.com.spellnet.databinding.DeckDetailFragmentBinding
import br.com.spellnet.decklist.viewmodel.DeckDetailViewModel
import br.com.spellnet.model.deck.Deck
import org.koin.android.viewmodel.ext.android.viewModel

class DeckDetailFragment : Fragment() {

    companion object {
        private const val KEY_ARGS_DECK = "KEY_ARGS_DECK"

        fun newInstance(deck: Deck) = DeckDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DeckDetailFragment.KEY_ARGS_DECK, deck)
            }
        }
    }

    private val deckDetailsViewModel: DeckDetailViewModel by viewModel()

    private lateinit var binding: DeckDetailFragmentBinding

    private var deckDetailAdapter: DeckDetailAdapter? = null

    private val deck by lazy { this.arguments?.getParcelable(DeckDetailFragment.KEY_ARGS_DECK) as Deck? }

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
        deckDetailAdapter = DeckDetailAdapter(deck)
        binding.cardList.adapter = deckDetailAdapter
    }

    private fun bindToViewModel(deck: Deck) {
        deckDetailsViewModel.openDeck(deck).map { cardEntry ->
            cardEntry.value.observe(this, Observer { resource ->
                safeLet(deckDetailAdapter, resource) { adapter, resource ->
                    adapter.updateCardPricing(cardEntry.key, resource)

                }
            })
        }

    }

}
