package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.R
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.hide
import br.com.spellnet.commom.safeLet
import br.com.spellnet.commom.show
import br.com.spellnet.databinding.DeckListFragmentBinding
import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.deck.Deck
import com.crashlytics.android.Crashlytics
import org.koin.android.viewmodel.ext.android.viewModel

class DeckListFragment : Fragment() {

    companion object {
        fun newInstance() = DeckListFragment()
    }

    private val deckListViewModel : DeckListViewModel by viewModel()

    private lateinit var binding: DeckListFragmentBinding
    private var deckListAdapter: DeckListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DeckListFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        return binding.root
    }

    private fun bindViewComponents() {
        deckListAdapter = DeckListAdapter(mutableListOf()) {
            deckListViewModel.openDeck(it)
        }
        binding.deckList.adapter = deckListAdapter

        binding.addDeckButton.setOnClickListener {
            deckListViewModel.addDeck()
        }
    }

    private fun bindToViewModel() {
        deckListViewModel.action().observe(this, Observer {
            when(it) {
                is DeckListViewModel.Action.AddDeck -> {
                    openAddDeck()
                }
                is DeckListViewModel.Action.OpenDeck -> {
                    openDeckDetails(it.deck)
                }
            }
        })

        deckListViewModel.deckList().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    handleDeckList(it.data)
                }
            }
        })
    }

    private fun handleDeckList(deckList: List<Deck>) {
        if (deckList.isEmpty()) {
            binding.emptyState.show()
            binding.deckList.hide()
        } else {
            binding.emptyState.hide()
            binding.deckList.show()
            deckListAdapter?.updateDeckList(deckList)
        }
    }

    private fun openAddDeck() {
        val addDeckFragment = AddDeckFragment.newInstance(object : AddDeckFragment.ResultListener {
            override fun onDeckSaved(deck: Deck) {
                deckListViewModel.retryDeckList()
            }
        })
        addDeckFragment.show(fragmentManager, addDeckFragment::javaClass.name)
    }

    private fun openDeckDetails(deck: Deck) {
        // TODO change to a centralized method
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, DeckDetailFragment.newInstance(deck), DeckDetailFragment::class.java.simpleName) // Add this transaction to the back stack (name is an optional name for this back stack state, or null).
            ?.addToBackStack(null)
            ?.commit()
    }
}
