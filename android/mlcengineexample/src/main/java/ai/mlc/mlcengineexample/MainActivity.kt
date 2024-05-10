package ai.mlc.mlcengineexample

import ChatCompletionStreamResponse
import ai.mlc.mlcengineexample.ui.theme.MLCChatTheme
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.gson.Gson
import java.io.File


class MainActivity : ComponentActivity() {
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var modelPath = "phi-2-q4f16_1"
        modelPath = File(application.getExternalFilesDir(""), modelPath).toString()
        Log.i("MLC", "model path: $modelPath")
        val modelLib = "system://phi_msft_q4f16_1"
        val engineConfigJSONStr = """
            {
                "model": "$modelPath",
                "model_lib": "$modelLib",
                "mode": "interactive"
            }
        """.trimIndent()
        Log.i("MLC", "engine loaded")
        val jsonRequest = """
            {
                "model": "phi-2-q4f16_1",
                "messages": [
                    {
                        "role": "user",
                        "content": [
                            { "type": "text", "text": "Tell me about Pittsburgh?" }
                        ]
                    }
                ]
            }
        """.trimIndent()

        setContent {
            val responseText = remember { mutableStateOf("") }
            fun handleStreamCallback(text: String) {
                // parse text to ChatCompletionStreamResponse
                val streamResponse = gson.fromJson(text, ChatCompletionStreamResponse::class.java)
                runOnUiThread {
                    responseText.value += streamResponse.choices[0].delta.content?.get(0)?.get("text")
                }
            }
            val engine = MLCEngine(::handleStreamCallback)
            engine.reload(engineConfigJSONStr)
            val response = engine.chatCompletion(jsonRequest)
            Log.i("MLC", "response: $response")
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MLCChatTheme {
                    Text(text = responseText.value)
                }
            }
        }
    }
}