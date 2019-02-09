package br.com.spellnet.model.deck

import br.com.spellnet.commom.BaseServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.IOException


class DeckService(private val deckParser: DeckParser) {

    suspend fun importDeck(deckImport: DeckImport): Deck? = withContext(Dispatchers.IO) {
        val client = BaseServer.buildOkHttpClient()

        val request = Request.Builder()
            .url(deckImport.url)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val deckResponseString = response.body()?.string()

                Deck(deckImport.name, deckParser.parse(deckResponseString))
            } else null
        } catch (e: DeckParseException) {
            null
        } catch (e: IOException) {
            null
        }
    }

}