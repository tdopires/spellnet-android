package br.com.spellnet.decklist.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.spellnet.R
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.hideSoftInput
import br.com.spellnet.commom.showSoftInput
import br.com.spellnet.databinding.AddDeckFragmentBinding
import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.model.deck.Deck
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable


class AddDeckFragment : DialogFragment() {

    companion object {
        private const val KEY_ARGS_RESULT_LISTENER = "KEY_ARGS_RESULT_LISTENER"
        private const val KEY_ARGS_DECK_URL_TO_IMPORT = "KEY_ARGS_DECK_URL_TO_IMPORT"

        fun newInstance(deckUrlToImport: String?, resultListener: AddDeckResultListener) = AddDeckFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ARGS_DECK_URL_TO_IMPORT, deckUrlToImport)
                putSerializable(KEY_ARGS_RESULT_LISTENER, resultListener)
            }
        }
    }

    private val addDeckViewModel: AddDeckViewModel by viewModel()

    private lateinit var binding: AddDeckFragmentBinding

    private val deckUrlToImport by lazy { this.arguments?.getString(KEY_ARGS_DECK_URL_TO_IMPORT) }
    private val resultListener by lazy { this.arguments?.getSerializable(KEY_ARGS_RESULT_LISTENER) as AddDeckResultListener? }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddDeckFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        return binding.root
    }

    private fun bindViewComponents() {
        binding.deckForm = AddDeckForm().apply {
            url = deckUrlToImport
        }
        binding.saveButton.setOnClickListener {
            //addDeckViewModel.onSaveDeck(binding.deckForm)
            val result = AddDeckForm()
            result.name = binding.deckName.text.toString()
            result.url = binding.deckUrl.text.toString()
            addDeckViewModel.onSaveDeck(result)
        }
        binding.deckName.showSoftInput()
    }

    private fun bindToViewModel() {
        addDeckViewModel.saveDeck().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, R.string.add_deck_success, Toast.LENGTH_LONG).show()
                    this@AddDeckFragment.dismiss()
                    binding.deckName.hideSoftInput()
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

interface AddDeckResultListener : Serializable {
    fun onDeckSaved(deck: Deck)
}
