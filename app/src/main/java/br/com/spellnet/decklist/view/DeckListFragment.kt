package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.safeLet
import br.com.spellnet.databinding.DeckListFragmentBinding
import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.deck.Deck

class DeckListFragment : Fragment() {

    companion object {
        fun newInstance() = DeckListFragment()
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(DeckListViewModel::class.java) }

    private lateinit var binding: DeckListFragmentBinding
    private var deckListAdapter: DeckListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DeckListFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        return binding.root
    }

    private fun bindViewComponents() {
        deckListAdapter = DeckListAdapter(mutableListOf())
        binding.deckList.layoutManager = LinearLayoutManager(context)
        binding.deckList.adapter = deckListAdapter

        binding.addDeckButton.setOnClickListener {
            viewModel.addDeck()
        }
    }

    private fun bindToViewModel() {
        viewModel.action().observe(this, Observer {
            when(it) {
                is DeckListViewModel.Action.AddDeck -> {
                    openAddDeck()
                }
                is DeckListViewModel.Action.OpenDeck -> {
                    openDeckDetails(it.deck)
                }
            }
        })

        viewModel.deckList().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    safeLet(deckListAdapter, it.data) { adapter, deckList ->
                        adapter.updateDeckList(deckList)
                    }
                }
            }
        })
    }

    private fun openAddDeck() {
        val addDeckFragment = AddDeckFragment.newInstance()
        addDeckFragment.show(fragmentManager, addDeckFragment::javaClass.name)
    }

    private fun openDeckDetails(deck: Deck) {

    }
}
