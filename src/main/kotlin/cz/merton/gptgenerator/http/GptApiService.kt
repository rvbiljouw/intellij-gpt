package cz.merton.gptgenerator.http

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface GptApiService {

    @Headers("Content-Type: application/json")
    @POST("completions")
    suspend fun generateCode(
        @Header("Authorization") apiKey: String,
        @Body request: GptRequest
    ): Response<GptResponse>

}

data class GptRequest(
    val prompt: String,
    val temperature: Double,
    @SerializedName("max_tokens")
    val maxTokens: Int,
    val model: String = "text-davinci-003",
    val n: Int = 1
)

data class GptResponse(
    val id: String,
    val choices: List<GptChoice>
)

data class GptChoice(
    val index: Int,
    val text: String
)