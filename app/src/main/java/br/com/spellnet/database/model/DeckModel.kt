package br.com.spellnet.database.model

import androidx.room.Embedded
import androidx.room.Relation
import br.com.spellnet.database.entity.CardEntity
import br.com.spellnet.database.entity.DeckCardQuantityEntity
import br.com.spellnet.database.entity.DeckEntity
import br.com.spellnet.database.entity.DeckSectionEntity


data class DeckAndSections(
    @Embedded val deck: DeckEntity,
    @Relation(parentColumn = "id", entity = DeckSectionEntity::class, entityColumn = "deck_id")
    val deckSections: List<DeckSectionAndCardQuantities>
)

data class DeckSectionAndCardQuantities(
    @Embedded val deckSection: DeckSectionEntity,
    @Relation(parentColumn = "id", entity = DeckCardQuantityEntity::class, entityColumn = "deck_section_id")
    val deckCardQuantities: List<DeckCardQuantitiesAndCards>
)

data class DeckCardQuantitiesAndCards(
    @Embedded val deckCardQuantityEntity: DeckCardQuantityEntity,
    @Relation(parentColumn = "card_id", entity = CardEntity::class, entityColumn = "id")
    val card: List<CardEntity>
)