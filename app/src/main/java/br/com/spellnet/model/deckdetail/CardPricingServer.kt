package br.com.spellnet.model.deckdetail

import br.com.spellnet.commom.BaseServer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CardPricingServer {

    private val gson: Gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(BaseServer.buildOkHttpClient())
        .baseUrl("https://spellnet.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

}
