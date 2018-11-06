package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.commom.Resource
import br.com.spellnet.databinding.AddDeckFragmentBinding
import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class AddDeckFragment : DialogFragment() {

    companion object {
        fun newInstance() = AddDeckFragment()
    }

    private val addDeckViewModel: AddDeckViewModel by viewModel()

    private lateinit var binding: AddDeckFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddDeckFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        return binding.root
    }

    private fun bindViewComponents() {
        binding.deckForm = addDeckViewModel.deck()
        binding.setLifecycleOwner(this)

        binding.saveButton.setOnClickListener {
            addDeckViewModel.onSaveDeck(binding.deckForm)
        }
    }

    private fun bindToViewModel() {
        addDeckViewModel.saveDeck().observe(this, Observer {
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
