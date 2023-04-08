import cz.merton.gptgenerator.exception.RequestFailedException
import cz.merton.gptgenerator.http.GptApiService
import cz.merton.gptgenerator.http.GptRequest
import cz.merton.gptgenerator.settings.ChatGptConfigurable
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatGptPlugin {
    private val gptApiBaseUrl = "https://api.openai.com/v1/"
    private val gptApiService: GptApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(gptApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gptApiService = retrofit.create(GptApiService::class.java)
    }

    fun chat(prompt: String): String {
        val apiKey = ChatGptConfigurable.getApiKey()
            ?: throw RuntimeException("Missing API key - configure it in the plugin settings")

        val request = GptRequest(prompt = prompt, temperature = 0.7, maxTokens = 1024)
        val response = runBlocking {
            gptApiService.generateCode("Bearer $apiKey", request)
        }

        if (!response.isSuccessful) {
            throw RequestFailedException("Request failed: ${response.errorBody()?.string()}")
        }
        return response.body()?.choices?.firstOrNull()?.text ?: ""
    }

}
