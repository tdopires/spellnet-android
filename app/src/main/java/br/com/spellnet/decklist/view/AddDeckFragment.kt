package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.safeLet
import br.com.spellnet.databinding.AddDeckFragmentBinding
import br.com.spellnet.databinding.DeckListFragmentBinding
import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.deck.Deck

class AddDeckFragment : DialogFragment() {

    companion object {
        fun newInstance() = AddDeckFragment()
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(AddDeckViewModel::class.java) }

    private lateinit var binding: AddDeckFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddDeckFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        return binding.root
    }

    private fun bindViewComponents() {
        binding.deckForm = viewModel.deck()
        binding.setLifecycleOwner(this)

        binding.saveButton.setOnClickListener {
            viewModel.onSaveDeck()
        }
    }

    private fun bindToViewModel() {
        viewModel.saveDeck().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Log.d("SAVE_DECK", "Success")
                }
                is Resource.Loading -> {
                    Log.d("SAVE_DECK", "Loading")
                }
                is Resource.Error -> {
                    Log.d("SAVE_DECK", "Error")
                }
            }
        })
    }

}
