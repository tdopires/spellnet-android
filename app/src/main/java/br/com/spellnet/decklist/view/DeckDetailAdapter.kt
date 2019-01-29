package br.com.spellnet.decklist.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.R
import br.com.spellnet.databinding.CardPricingListRowBinding
import br.com.spellnet.model.card.CardPricing

class DeckDetailAdapter(private val cardPricingList: MutableList<CardPricing>) :
    RecyclerView.Adapter<DeckDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DeckDetailAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_pricing_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeckDetailAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        binding?.cardPricing = cardPricingList[position]
        binding?.executePendingBindings()
    }

    override fun getItemCount() = cardPricingList.size

    fun updateCardPricing(newCardPricing: CardPricing) {
        val indexOf = cardPricingList.indexOfFirst { it.card == newCardPricing.card }
        if (indexOf != -1) {
            cardPricingList[indexOf] = newCardPricing
            notifyItemChanged(indexOf)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: CardPricingListRowBinding? = DataBindingUtil.bind(itemView)
    }

}
