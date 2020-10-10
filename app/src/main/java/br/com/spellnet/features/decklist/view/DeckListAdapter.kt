package br.com.spellnet.features.decklist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.spellnet.R
import br.com.spellnet.databinding.DeckListRowBinding
import br.com.spellnet.entity.Deck

class DeckListAdapter(
    private val deckList: MutableList<Deck>,
    private val onClickListener: ((Deck) -> Unit)?,
    private val onLongClickListener: ((Deck) -> Unit)?,
) : RecyclerView.Adapter<DeckListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DeckListAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.deck_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeckListAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        binding?.deck = deckList[position]
        binding?.deckInfo?.setOnClickListener {
            onClickListener?.invoke(deckList[position])
        }
        binding?.deckInfo?.setOnLongClickListener {
            onLongClickListener?.invoke(deckList[position])
            true
        }
        binding?.executePendingBindings()
    }

    override fun getItemCount() = deckList.size

    fun updateDeckList(newDeckList: List<Deck>) {
        deckList.clear()
        deckList.addAll(newDeckList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DeckListRowBinding? = DataBindingUtil.bind(itemView)
    }
}
