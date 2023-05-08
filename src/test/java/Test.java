import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.interceptor.OpenAILogger;
import com.openai.utils.CommonUtil;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.github.asleepyfish.service.OpenAiProxyService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        ObjectMapper mapper = OpenAiProxyService.defaultObjectMapper();
        // Create proxy object
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",19180));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = OpenAiProxyService.defaultClient("", Duration.ofSeconds(10L))
                .newBuilder()
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = OpenAiProxyService.defaultRetrofit(client, mapper);
        OpenAiService service = new OpenAiService(retrofit.create(OpenAiApi.class),client.dispatcher().executorService());
        System.out.println("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "如何学习java");
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);

        service.shutdownExecutor();
    }
}
