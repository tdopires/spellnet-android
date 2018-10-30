package br.com.spellnet.ui.decklist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.R

class DeckListFragment : Fragment() {

    companion object {
        fun newInstance() = DeckListFragment()
    }

    private lateinit var viewModel: DeckListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.deck_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeckListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
