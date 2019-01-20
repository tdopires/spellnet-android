package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.spellnet.R
import br.com.spellnet.commom.Resource
import br.com.spellnet.databinding.AddDeckFragmentBinding
import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.model.deck.Deck
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable


class AddDeckFragment : DialogFragment() {

    companion object {
        private const val RESULT_LISTENER = "RESULT_LISTENER"

        fun newInstance(resultListener: ResultListener) = AddDeckFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RESULT_LISTENER, resultListener)
            }
        }
    }

    interface ResultListener : Serializable {
        fun onDeckSaved(deck: Deck)
    }

    private val addDeckViewModel: AddDeckViewModel by viewModel()

    private lateinit var binding: AddDeckFragmentBinding

    private val resultListener by lazy { this.arguments?.getSerializable(RESULT_LISTENER) as ResultListener? }

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
        binding.deckName.requestFocus()
    }

    private fun bindToViewModel() {
        addDeckViewModel.saveDeck().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, R.string.add_deck_success, Toast.LENGTH_LONG).show()
                    this@AddDeckFragment.dismiss()
                    resultListener?.onDeckSaved(it.data)
                }
                is Resource.Loading -> {
                    Toast.makeText(context, R.string.loading, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, R.string.add_deck_error, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}
