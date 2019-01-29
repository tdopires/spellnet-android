package br.com.spellnet.model.deck

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class DeckService(private val deckParser: DeckParser) {

    suspend fun importDeck(deckImport: DeckImport): Deck? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(deckImport.url)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            try {
                val deckResponseString = response.body().string()

                Deck(deckImport.name, deckParser.parse(deckResponseString))
            } catch (e: IOException) {
                null
            }
        } else null
    }

}