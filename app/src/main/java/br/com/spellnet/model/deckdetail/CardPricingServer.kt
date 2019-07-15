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
        .baseUrl("http://3.93.71.162:3000/") //TODO let user change this for webserver flexibilization
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

}
