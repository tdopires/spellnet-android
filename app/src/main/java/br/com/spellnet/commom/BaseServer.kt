package br.com.spellnet.commom

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object BaseServer {

    fun buildOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}