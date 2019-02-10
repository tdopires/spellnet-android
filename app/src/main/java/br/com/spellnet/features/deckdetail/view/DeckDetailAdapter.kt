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

class DeckDetailAdapter(deck: Deck) : RecyclerView.Adapter<DeckDetailAdapter.DeckDetailViewHolder>() {

    private val viewItems = mutableListOf<ViewItem>()

    var onCardPricingRetryClickListener: ((Card, Resource<CardPricing>) -> Unit)? = null
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
                bindCardViewItemComponents(binding, viewItem)
                binding?.haveCardQuantity = viewItem.haveCardQuantity
                binding?.cardPricing = viewItem.resourceCardPricing
                binding?.haveAll = viewItem.haveCardQuantity?.quantity == viewItem.cardQuantity.quantity
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

    private fun bindCardViewItemComponents(
        binding: DeckDetailCardPricingListRowBinding?,
        viewItem: ViewItem.CardViewItem
    ) {
        binding?.root?.let {
            it.setOnClickListener {
                onCardPricingRetryClickListener?.invoke(viewItem.cardQuantity.card, viewItem.resourceCardPricing)
            }

            val myArray = arrayListOf<CharSequence>()
            for (i in 0..sumNeededCardQuantity(viewItem.cardQuantity.card)) {
                myArray.add("$i")
            }
            val cardQuantityAdapter =
                ArrayAdapter(it.context, android.R.layout.simple_spinner_item, myArray)
            cardQuantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerHaveCardQuantity.adapter = cardQuantityAdapter

            binding.spinnerHaveCardQuantity.setSelection(viewItem.haveCardQuantity?.quantity ?: 0, false)

            binding.spinnerHaveCardQuantity.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                        onHaveCardQuantityChangedListener?.invoke(CardQuantity(pos, viewItem.cardQuantity.card))
                    }
                }

            binding.checkboxHaveCardQuantity.setOnCheckedChangeListener(null)
            binding.checkboxHaveCardQuantity.isChecked =
                viewItem.haveCardQuantity?.quantity == viewItem.cardQuantity.quantity

            binding.checkboxHaveCardQuantity.setOnCheckedChangeListener { _, checked ->
                val card = viewItem.cardQuantity.card
                onHaveCardQuantityChangedListener?.invoke(
                    CardQuantity(
                        if (checked) sumNeededCardQuantity(card) else 0,
                        card
                    )
                )
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
