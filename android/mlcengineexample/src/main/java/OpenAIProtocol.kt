import java.util.*

// Data classes for v1/chat/completions
// API reference: https://platform.openai.com/docs/api-reference/chat/create

data class TopLogProbs(
    val token: String,
    val logprob: Float,
    val bytes: List<Int>? = null
)

data class LogProbsContent(
    val token: String,
    val logprob: Float,
    var bytes: List<Int>? = null,
    var top_logprobs: List<TopLogProbs> = listOf()
)

data class LogProbs(
    var content: List<LogProbsContent> = listOf()
)

data class ChatFunction(
    val name: String,
    var description: String? = null,
    val parameters: Map<String, String>
)

data class ChatTool(
    val type: String = "function",
    val function: ChatFunction
)

data class ChatFunctionCall(
    val name: String,
    // NOTE: arguments should be dict str to any codable
    // for now only allow string output due to typing issues
    var arguments: Map<String, String>? = null
)

data class ChatToolCall(
    val id: String = UUID.randomUUID().toString(),
    val type: String = "function",
    val function: ChatFunctionCall
)

data class ChatCompletionMessage(
    val role: String,
    var content: List<Map<String, String>>? = null,
    var name: String? = null,
    var tool_calls: List<ChatToolCall>? = null,
    var tool_call_id: String? = null
)

data class ChatCompletionStreamResponseChoice(
    var finish_reason: String? = null,
    val index: Int,
    val delta: ChatCompletionMessage,
    var lobprobs: LogProbs? = null
)

data class ChatCompletionStreamResponse(
    val id: String,
    var choices: List<ChatCompletionStreamResponseChoice> = listOf(),
    var created: Int? = null,
    var model: String? = null,
    val system_fingerprint: String,
    var `object`: String? = null
)