package br.com.spellnet.features.deckdetail.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.spellnet.R
import br.com.spellnet.commom.Resource
import br.com.spellnet.databinding.DeckDetailCardPricingListRowBinding
import br.com.spellnet.databinding.DeckDetailNameListRowBinding
import br.com.spellnet.databinding.DeckDetailSectionTitleListRowBinding
import br.com.spellnet.databinding.DeckDetailTotalValueListRowBinding
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import br.com.spellnet.entity.CardQuantity
import br.com.spellnet.entity.Deck

private const val CARD_VIEW_ITEM = 0
private const val NAME_VIEW_ITEM = 1
private const val SECTION_TITLE_VIEW_ITEM = 2
private const val TOTAL_VALUE_VIEW_ITEM = 3

class DeckDetailAdapter(deck: Deck,
                        private val onClickListener: ((Card, Resource<CardPricing>) -> Unit)?) :
    RecyclerView.Adapter<DeckDetailAdapter.DeckDetailViewHolder>() {

    private val viewItems = mutableListOf<ViewItem>()

    sealed class ViewItem {
        class NameViewItem(val deckName: String) : ViewItem()
        class SectionTitleViewItem(val sectionTitle: String) : ViewItem()
        class CardViewItem(
            val cardQuantity: CardQuantity,
            var resourceCardPricing: Resource<CardPricing> = Resource.Loading()
        ) : ViewItem()

        class TotalValueViewItem(var deckTotalValue: Double = 0.0) : ViewItem()
    }

    init {
        viewItems.add(ViewItem.NameViewItem(deck.name))
        deck.sections.forEach {
            viewItems.add(ViewItem.SectionTitleViewItem(it.title))
            it.cardList.forEach {
                viewItems.add(ViewItem.CardViewItem(it))
            }
        }
        viewItems.add(ViewItem.TotalValueViewItem())
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewItems[position]) {
            is ViewItem.NameViewItem -> NAME_VIEW_ITEM
            is ViewItem.SectionTitleViewItem -> SECTION_TITLE_VIEW_ITEM
            is ViewItem.CardViewItem -> CARD_VIEW_ITEM
            is ViewItem.TotalValueViewItem -> TOTAL_VALUE_VIEW_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckDetailViewHolder {
        return when (viewType) {
            NAME_VIEW_ITEM -> DeckDetailViewHolder.NameViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_name_list_row, parent, false)
            )
            SECTION_TITLE_VIEW_ITEM -> DeckDetailViewHolder.SectionTitleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_section_title_list_row, parent, false)
            )
            TOTAL_VALUE_VIEW_ITEM -> DeckDetailViewHolder.TotalValueViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_total_value_list_row, parent, false)
            )
            else -> DeckDetailViewHolder.CardViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_card_pricing_list_row, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: DeckDetailViewHolder, position: Int) {
        val viewItem = viewItems[position]
        when {
            holder is DeckDetailViewHolder.NameViewHolder && viewItem is ViewItem.NameViewItem -> {
                val binding = holder.binding
                binding?.deckName = viewItem.deckName
                binding?.executePendingBindings()
            }
            holder is DeckDetailViewHolder.SectionTitleViewHolder && viewItem is ViewItem.SectionTitleViewItem -> {
                val binding = holder.binding
                binding?.deckSectionTitle = viewItem.sectionTitle
                binding?.executePendingBindings()
            }
            holder is DeckDetailViewHolder.CardViewHolder && viewItem is ViewItem.CardViewItem -> {
                val binding = holder.binding
                binding?.root?.setOnClickListener {
                    onClickListener?.invoke(viewItem.cardQuantity.card, viewItem.resourceCardPricing)
                }
                binding?.cardPricing = viewItem.resourceCardPricing
                binding?.cardQuantity = viewItem.cardQuantity
                binding?.executePendingBindings()
            }
            holder is DeckDetailViewHolder.TotalValueViewHolder && viewItem is ViewItem.TotalValueViewItem -> {
                val binding = holder.binding
                binding?.deckTotalValue = viewItem.deckTotalValue
                binding?.executePendingBindings()
            }
        }
    }

    override fun getItemCount() = viewItems.size

    fun updateCardPricing(card: Card, resourceCardPricing: Resource<CardPricing>) {
        val cardsIndexOf = mutableListOf<Int>()
        viewItems.forEachIndexed { index, viewItem ->
            if (viewItem is ViewItem.CardViewItem && viewItem.cardQuantity.card.name.equals(card.name, ignoreCase = true)) {
                cardsIndexOf.add(index)
            }
        }
        cardsIndexOf.forEach { indexOfCard ->
            if (indexOfCard in 0..(viewItems.size - 1)) {
                (viewItems[indexOfCard] as ViewItem.CardViewItem).resourceCardPricing = resourceCardPricing
                notifyItemChanged(indexOfCard)
            } else {
                Log.d("DeckDetailsAdapter", "indexOfCard failed = $indexOfCard / viewItems.size = ${viewItems.size}")
            }
        }

        updateDeckTotalValue()
    }

    private fun updateDeckTotalValue() {
        val indexOfTotalValue = viewItems.size - 1
        if (viewItems[indexOfTotalValue] is ViewItem.TotalValueViewItem) {
            (viewItems[indexOfTotalValue] as ViewItem.TotalValueViewItem).deckTotalValue =
                    viewItems.filter { it is ViewItem.CardViewItem }
                        .map {
                            val cardViewItem = (it as ViewItem.CardViewItem)
                            val cardViewItemCardPricing = cardViewItem.resourceCardPricing
                            if (cardViewItemCardPricing is Resource.Success) {
                                cardViewItemCardPricing.data.minPrice?.let { cardPricingMinPrice ->
                                    cardViewItem.cardQuantity.quantity.toDouble() * cardPricingMinPrice.toDouble()
                                } ?: run { 0.0 }
                            } else 0.0

                        }.sum()
            notifyItemChanged(indexOfTotalValue)
        }
    }

    sealed class DeckDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class NameViewHolder(itemView: View) : DeckDetailViewHolder(itemView) {
            val binding: DeckDetailNameListRowBinding? = DataBindingUtil.bind(itemView)
        }

        class SectionTitleViewHolder(itemView: View) : DeckDetailViewHolder(itemView) {
            val binding: DeckDetailSectionTitleListRowBinding? = DataBindingUtil.bind(itemView)
        }

        class CardViewHolder(itemView: View) : DeckDetailViewHolder(itemView) {
            val binding: DeckDetailCardPricingListRowBinding? = DataBindingUtil.bind(itemView)
        }

        class TotalValueViewHolder(itemView: View) : DeckDetailViewHolder(itemView) {
            val binding: DeckDetailTotalValueListRowBinding? = DataBindingUtil.bind(itemView)
        }
    }

}
