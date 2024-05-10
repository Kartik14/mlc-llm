package ai.mlc.mlcengineexample

import ai.mlc.mlcllm.JSONFFIEngine
import android.util.Log
import kotlin.concurrent.thread

class MLCEngine (private val streamCallback: (String) -> Unit) {
    private val jsonFFIEngine = JSONFFIEngine()

    init {
        jsonFFIEngine.initBackgroundEngine(streamCallback)
        thread(start = true) {
            jsonFFIEngine.runBackgroundLoop()
        }
        thread(start = true) {
            jsonFFIEngine.runBackgroundStreamBackLoop()
        }
    }

//    private fun streamCallback(text: String) {
//        Log.i("MLCEngine", "streamCallback: $text")
//    }

    private fun deinit() {
        jsonFFIEngine.exitBackgroundLoop()
    }

    fun reload(engineConfigJSONStr: String) {
        jsonFFIEngine.reload(engineConfigJSONStr)
    }

    private fun unload() {
        jsonFFIEngine.unload()
    }

    fun chatCompletion(requestJSONStr: String) {
        val requestId = "123"
        jsonFFIEngine.chatCompletion(requestJSONStr, requestId)
    }
}