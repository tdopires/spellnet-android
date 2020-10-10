package br.com.spellnet.model.decklist

import br.com.spellnet.commom.BaseServer
import br.com.spellnet.entity.Deck
import br.com.spellnet.entity.DeckImport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.IOException


class DeckService(private val deckParser: DeckParser) {

    suspend fun importDeck(deckImport: DeckImport): Deck? = withContext(Dispatchers.IO) {
        val client = BaseServer.buildOkHttpClient()

        try {
            val request = Request.Builder()
                .url(deckImport.url)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val deckResponseString = response.body()?.string()

                Deck(null, deckImport.name, deckImport.url, deckParser.parse(deckResponseString))
            } else null
        } catch (e: DeckParseException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        } catch (e: IOException) {
            null
        }
    }

}