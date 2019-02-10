package br.com.spellnet.features.deckdetail.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

class DeckDetailAdapter(deck: Deck) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewItems = mutableListOf<ViewItem>()

    var onCardPricingRetryClickListener: ((CardQuantity, Resource<CardPricing>) -> Unit)? = null
    var onHaveCardQuantityChangedListener: ((CardQuantity) -> Unit)? = null

    sealed class ViewItem {
        class NameViewItem(val deckName: String) : ViewItem()
        class SectionTitleViewItem(val sectionTitle: String) : ViewItem()
        class CardViewItem(
            val cardQuantity: CardQuantity,
            var haveCardQuantity: CardQuantity? = null,
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

    override fun getItemCount() = viewItems.size

    override fun getItemViewType(position: Int): Int {
        return when (viewItems[position]) {
            is ViewItem.NameViewItem -> NAME_VIEW_ITEM
            is ViewItem.SectionTitleViewItem -> SECTION_TITLE_VIEW_ITEM
            is ViewItem.CardViewItem -> CARD_VIEW_ITEM
            is ViewItem.TotalValueViewItem -> TOTAL_VALUE_VIEW_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NAME_VIEW_ITEM -> NameViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_name_list_row, parent, false)
            )
            SECTION_TITLE_VIEW_ITEM -> SectionTitleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_section_title_list_row, parent, false)
            )
            TOTAL_VALUE_VIEW_ITEM -> TotalValueViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.deck_detail_total_value_list_row, parent, false)
            )
            else -> {
                val cardViewHolder = CardViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.deck_detail_card_pricing_list_row, parent, false)
                )
                createCardViewItemComponents(cardViewHolder)
                return cardViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewItem = viewItems[position]
        when {
            holder is NameViewHolder && viewItem is ViewItem.NameViewItem -> {
                holder.binding?.let {
                    it.deckName = viewItem.deckName
                    it.executePendingBindings()
                }

            }
            holder is SectionTitleViewHolder && viewItem is ViewItem.SectionTitleViewItem -> {
                holder.binding?.let {
                    it.deckSectionTitle = viewItem.sectionTitle
                    it.executePendingBindings()
                }
            }
            holder is CardViewHolder && viewItem is ViewItem.CardViewItem -> {
                holder.binding?.let {
                    it.spinnerHaveCardQuantity.setSelection(viewItem.haveCardQuantity?.quantity ?: 0, false)

                    it.haveCardQuantity = viewItem.haveCardQuantity
                    it.cardPricing = viewItem.resourceCardPricing
                    it.cardQuantity = viewItem.cardQuantity
                    it.executePendingBindings()
                }
            }
            holder is TotalValueViewHolder && viewItem is ViewItem.TotalValueViewItem -> {
                holder.binding?.let {
                    it.deckTotalValue = viewItem.deckTotalValue
                    it.executePendingBindings()
                }
            }
        }
    }

    private fun createCardViewItemComponents(viewHolder: CardViewHolder) {
        viewHolder.binding?.let {
            it.root.setOnClickListener {
                if (viewHolder.adapterPosition < 0) return@setOnClickListener
                val viewItem = viewItems[viewHolder.adapterPosition] as ViewItem.CardViewItem
                onCardPricingRetryClickListener?.invoke(viewItem.cardQuantity, viewItem.resourceCardPricing)
            }

            val myArray = arrayListOf<CharSequence>()
            for (i in 0..100) { // TODO  change to textView/editText
                myArray.add("$i")
            }
            val cardQuantityAdapter =
                ArrayAdapter(it.root.context, android.R.layout.simple_spinner_item, myArray)
            cardQuantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.spinnerHaveCardQuantity.adapter = cardQuantityAdapter

            it.spinnerHaveCardQuantity.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                        if (viewHolder.adapterPosition < 0) return
                        val viewItem = viewItems[viewHolder.adapterPosition] as ViewItem.CardViewItem
                        onHaveCardQuantityChangedListener?.invoke(CardQuantity(pos, viewItem.cardQuantity.card))
                    }
                }
        }
    }

    fun updateCardHaveQuantity(haveCardQuantity: CardQuantity) {
        val card = haveCardQuantity.card
        var haveLeftQuantity = haveCardQuantity.quantity
        indexesOfCard(card).forEach { index ->
            val viewItemCardQuantity = (viewItems[index] as ViewItem.CardViewItem).cardQuantity.quantity

            when {
                haveLeftQuantity == 0 -> {
                    (viewItems[index] as ViewItem.CardViewItem).haveCardQuantity = null
                }
                haveLeftQuantity >= viewItemCardQuantity -> {
                    (viewItems[index] as ViewItem.CardViewItem).haveCardQuantity =
                        CardQuantity(viewItemCardQuantity, card)
                    haveLeftQuantity -= viewItemCardQuantity
                }
                else -> {
                    (viewItems[index] as ViewItem.CardViewItem).haveCardQuantity = CardQuantity(haveLeftQuantity, card)
                    haveLeftQuantity = 0
                }
            }
            notifyItemChanged(index)
        }
        updateDeckTotalValue()
    }

    private fun sumNeededCardQuantity(card: Card): Int {
        return indexesOfCard(card).sumBy { (viewItems[it] as ViewItem.CardViewItem).cardQuantity.quantity }
    }

    private fun indexesOfCard(card: Card): MutableList<Int> {
        val indexOfCards = mutableListOf<Int>()
        viewItems.forEachIndexed { index, viewItem ->
            if (viewItem is ViewItem.CardViewItem && viewItem.cardQuantity.card.name.equals(
                    card.name,
                    ignoreCase = true
                )
            ) {
                indexOfCards.add(index)
            }
        }
        return indexOfCards
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
                                Math.max(
                                    0,
                                    cardViewItem.cardQuantity.quantity - (cardViewItem.haveCardQuantity?.quantity ?: 0)
                                ).toDouble() * cardPricingMinPrice.toDouble()
                            } ?: run { 0.0 }
                        } else 0.0

                    }.sum()
            notifyItemChanged(indexOfTotalValue)
        }
    }

    fun updateCardPricing(card: Card, resourceCardPricing: Resource<CardPricing>) {
        indexesOfCard(card).forEach { indexOfCard ->
            if (indexOfCard in 0..(viewItems.size - 1)) {
                (viewItems[indexOfCard] as ViewItem.CardViewItem).resourceCardPricing = resourceCardPricing
                notifyItemChanged(indexOfCard)
            } else {
                Log.d("DeckDetailsAdapter", "indexOfCard failed = $indexOfCard / viewItems.size = ${viewItems.size}")
            }
        }

        updateDeckTotalValue()
    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DeckDetailNameListRowBinding? = DataBindingUtil.bind(itemView)
    }

    class SectionTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DeckDetailSectionTitleListRowBinding? = DataBindingUtil.bind(itemView)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DeckDetailCardPricingListRowBinding? = DataBindingUtil.bind(itemView)
    }

    class TotalValueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DeckDetailTotalValueListRowBinding? = DataBindingUtil.bind(itemView)
    }

}
