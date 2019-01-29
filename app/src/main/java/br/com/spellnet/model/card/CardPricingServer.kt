package br.com.spellnet.model.card

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CardPricingServer {

    private val gson: Gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://3.88.16.63:3000/") //TODO let user change this for webserver flexibilization
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

}
