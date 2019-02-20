package br.com.spellnet.features.decklist.view

import android.arch.lifecycle.Observer
import android.content.DialogInterface
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
import br.com.spellnet.features.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.entity.Deck
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
        deckUrlToImport?.let {
            binding.deckUrl.append(it)
        }

        //TODO use two-way databinding on AddDeckForm object
        //TODO make a LoadingButton component
        binding.saveButton.setOnClickListener {
            binding.saveButton.text = "Importing deck..."
            binding.saveButton.isEnabled = false

            val result = AddDeckForm(name = binding.deckName.text.toString(), url = binding.deckUrl.text.toString())
            addDeckViewModel.onSaveDeck(result)
        }
        binding.deckName.showSoftInput()
    }

    private fun bindToViewModel() {
        addDeckViewModel.saveDeck().observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, R.string.add_deck_success, Toast.LENGTH_LONG).show()
                    resultListener?.onDeckSaved(it.data)
                    this@AddDeckFragment.dismiss()
                }
                is Resource.Loading -> {
                    Toast.makeText(context, R.string.loading, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.saveButton.isEnabled = true
                    Toast.makeText(context, R.string.add_deck_error, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface?) {
        binding.root.hideSoftInput()
        super.onDismiss(dialog)
    }

}

interface AddDeckResultListener : Serializable {
    fun onDeckSaved(deck: Deck)
}
