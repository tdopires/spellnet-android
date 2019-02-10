package br.com.spellnet.features.decklist.view

import android.databinding.BaseObservable
import android.databinding.Bindable
import br.com.spellnet.BR
import br.com.spellnet.commom.safeLet
import br.com.spellnet.entity.DeckImport


class AddDeckForm : BaseObservable() {

    var name: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    var url: String? = "https://www.mtggoldfish.com/deck/download/1555036" // null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.url)
        }

}

fun AddDeckForm.toDeckImport() =
    safeLet(this.name, this.url) { name, url ->
        DeckImport(name, url)
    }
