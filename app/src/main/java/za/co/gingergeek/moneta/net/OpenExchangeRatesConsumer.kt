package za.co.gingergeek.moneta.net

import android.content.Context
import com.android.volley.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import za.co.gingergeek.moneta.extensions.isNetworkConnected
import java.lang.reflect.Type

class OpenExchangeRatesConsumer(
    val context: Context,
    private val baseUrl: String,
    private val apiKey: String
) : OpenExchangeRatesAPI {

    private val requestManager: RequestManager = RequestManager(context)
    private val apiRepository = ApiRepository(context)

    override fun fetchCurrencies(callback: (ApiResponse<Map<String, String>>?) -> Unit) {
        if (context.isNetworkConnected()) {
            requestManager.makeRequest("$baseUrl/currencies.json",
                Response.Listener<String> { response ->
                    val type: Type = object : TypeToken<Map<String, String>>() {}.type
                    val data = Gson().fromJson<Map<String, String>>(response, type)
                    apiRepository.storeCurrencies(data)
                    callback.invoke(ApiResponse(data, true))
                },
                Response.ErrorListener {
                    callback.invoke(ApiResponse(null, false))
                }
            )
        } else {
            callback.invoke(null)
        }
    }

    override fun fetchExchangeRates(callback: (ApiResponse<ExchangeRateResponse>?) -> Unit) {
        if (context.isNetworkConnected()) {
            requestManager.makeRequest("$baseUrl/latest.json?app_id=$apiKey",
                Response.Listener<String> { response ->
                    val data = Gson().fromJson(response, ExchangeRateResponse::class.java)
                    apiRepository.storeExchangeRates(data.rates)
                    callback.invoke(ApiResponse(data, true))
                },
                Response.ErrorListener {
                    callback.invoke(ApiResponse(null, false))
                }
            )
        } else {
            callback.invoke(null)
        }
    }

    override fun dispose() {
        requestManager.clearQueue()
    }
}