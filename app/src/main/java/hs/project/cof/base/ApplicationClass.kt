package hs.project.cof.base

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass : Application() {

    companion object {
        // 서버 주소
        const val URL = "https://api.openai.com"
        // Retrofit
        lateinit var retrofit: Retrofit

        const val SEND_BY_BOT = 1
        const val SEND_BY_USER = 2
    }

    override fun onCreate() {
        super.onCreate()

        setRetrofit()
    }

    private fun setRetrofit() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}