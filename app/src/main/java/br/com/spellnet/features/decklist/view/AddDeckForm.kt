package br.com.spellnet.features.decklist.view

import br.com.spellnet.commom.safeLet
import br.com.spellnet.entity.DeckImport


class AddDeckForm(val name: String?, val url: String?)

fun AddDeckForm.toDeckImport() =
    safeLet(this.name, this.url) { name, url ->
        DeckImport(if (name.isBlank()) "(untitled)" else name, url)
    }
