import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.interceptor.OpenAILogger;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.github.asleepyfish.service.OpenAiProxyService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestOpenAI {
    public static void main(String[] args) {
        ObjectMapper mapper = OpenAiProxyService.defaultObjectMapper();
        // Create proxy object
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",19180));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = OpenAiProxyService.defaultClient("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",Duration.ofSeconds(10L))
                                .newBuilder()
                                .proxy(proxy)
                                .addInterceptor(httpLoggingInterceptor)
                                .build();
        Retrofit retrofit = OpenAiProxyService.defaultRetrofit(client, mapper);
        OpenAiService service = new OpenAiProxyService(retrofit.create(OpenAiApi.class));

        //OpenAiService service = new OpenAiProxyService("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","127.0.0.1",19180);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
//                .prompt("如何学习Java")
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(new ChatMessage("user", "如何学习Java")))
                .temperature(0.1)
                .stream(false)
                .build();

        List<ChatCompletionChoice> list =  service.createChatCompletion(completionRequest).getChoices();
        String result = list.stream().map(p -> {
            String content = p.getMessage().getContent();
            return content==null?"":content;
        }).collect(Collectors.joining(""));
        System.out.println("------------------------"+list.size());
        System.out.println(result);
//        for(ChatCompletionChoice co : list){
//            System.out.println(co.getMessage().getContent());
//        }
    }
}
