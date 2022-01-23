package com.ffcs.miaucats.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ffcs.miaucats.constants.Constants
import com.ffcs.miaucats.model.Imagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainViewModel : ViewModel() {
    private val mImagensLiveData = MutableLiveData<List<Imagem>>()
    private val mError = MutableLiveData<Boolean>()

    val imagens: LiveData<List<Imagem>>
        get() = mImagensLiveData

    val error: LiveData<Boolean>
        get() = mError

    fun fetchImagens() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url(Constants.IMGUR_BASE_URL +
                        "search/{{sort}}/{{window}}/{{page}}?q="
                        + Constants.searchBy)
                .method("GET", null)
                .addHeader("Authorization", "Client-ID " + Constants.clientID)
                .build()
            withContext(Dispatchers.Main) {
                client.newCall(request).enqueue(object : Callback {

                    override fun onResponse(call: Call, response: Response) {

                        if (response.isSuccessful) {
                            val data = JSONObject(response.body()!!.string())
                            parseDados(data)
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        mError.postValue(true)
                    }
                })
            }
        }
    }

    private fun parseDados(data: JSONObject) {
        val items: JSONArray = data.getJSONArray("data")
        val imagensFinais: ArrayList<Imagem> = ArrayList()

        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)

            if (item.has("images")) {
                val imagens: JSONArray = item.getJSONArray("images")
                for (count in 0 until imagens.length()) {
                    val img = imagens.getJSONObject(count)
                    val mImagem = Imagem(
                        img.getString("id"),
                        img.getString("description"),
                        img.getString("link")
                    )

                    imagensFinais.add(mImagem)
                }
            }
        }
        mImagensLiveData.postValue(imagensFinais)
    }
}