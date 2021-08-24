package br.com.spellnet.features.decklist.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.spellnet.R
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.hide
import br.com.spellnet.commom.show
import br.com.spellnet.databinding.DeckListFragmentBinding
import br.com.spellnet.entity.Deck
import br.com.spellnet.features.deckdetail.view.DeckDetailFragment
import br.com.spellnet.features.decklist.viewmodel.DeckListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeckListFragment : Fragment() {

    companion object {
        private const val KEY_ARGS_DECK_URL_TO_IMPORT = "KEY_ARGS_DECK_URL_TO_IMPORT"

        fun newInstance(deckUrlToImport: String?) = DeckListFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ARGS_DECK_URL_TO_IMPORT, deckUrlToImport)
            }
        }
    }

    private val deckListViewModel: DeckListViewModel by viewModel()

    private lateinit var binding: DeckListFragmentBinding
    private var deckListAdapter: DeckListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckListFragmentBinding.inflate(inflater, container, false)
        bindViewComponents()
        bindToViewModel()
        handleDeckUrlToImport()
        return binding.root
    }

    private fun handleDeckUrlToImport() {
        val deckUrlToImport = this.arguments?.getString(KEY_ARGS_DECK_URL_TO_IMPORT)
        if (deckUrlToImport != null) {
            deckListViewModel.addDeck(deckUrlToImport)
            this.arguments?.remove(KEY_ARGS_DECK_URL_TO_IMPORT)
        }
    }

    private fun bindViewComponents() {
        deckListAdapter = DeckListAdapter(mutableListOf(),
            onClickListener = {
                deckListViewModel.openDeck(it)
            },
            onLongClickListener = {
                deckListViewModel.deleteDeck(it)
            }
        )
        binding.deckList.adapter = deckListAdapter

        binding.addDeckButton.setOnClickListener {
            deckListViewModel.addDeck()
        }
    }

    private fun bindToViewModel() {
        deckListViewModel.action().observe(viewLifecycleOwner, Observer {
            when (it) {
                is DeckListViewModel.Action.AddDeck -> {
                    openAddDeck(it.deckUrlToImport)
                }
                is DeckListViewModel.Action.OpenDeck -> {
                    openDeckDetails(it.deck)
                }
                is DeckListViewModel.Action.DeleteDeck -> {
                    confirmDeckDeletion(it.deck)
                }
            }
        })

        deckListViewModel.deckList().observe(viewLifecycleOwner, Observer {
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

    private fun openAddDeck(deckUrlToImport: String?) {
        val addDeckFragment =
            AddDeckFragment.newInstance(deckUrlToImport, object : AddDeckResultListener {
                override fun onDeckSaved(deck: Deck) {
                    deckListViewModel.retryDeckList()
                }
            })
        addDeckFragment.show(requireFragmentManager(), addDeckFragment::javaClass.name)
    }

    private fun openDeckDetails(deck: Deck) {
        // TODO change to a centralized method
        val deckDetailFragment = DeckDetailFragment.newInstance(deck)
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
            )
            ?.replace(
                R.id.container,
                deckDetailFragment,
                DeckDetailFragment::class.java.simpleName
            )
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun confirmDeckDeletion(deck: Deck) {
        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.delete_deck_alert_title)
            .setMessage(resources.getString(R.string.delete_deck_alert_message, deck.name))
            .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                deckListViewModel.confirmDeleteDeck(deck)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}
