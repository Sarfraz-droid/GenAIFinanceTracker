package com.example.demo.Service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Entity.TransactionEntity;

public class OpenAIService {

    @Autowired
    public final ChatModel chatModel;

    public final OpenAiApi openAiApi;

    public OpenAIService() {
        this.openAiApi = OpenAiApi.builder().apiKey(System.getenv("SPRING_AI_OPENAI_API_KEY")).build();
        this.chatModel = OpenAiChatModel.builder().openAiApi(openAiApi).build();
    }
    public String getContextOfTransaction(TransactionEntity transaction) {
        ChatResponse chatResponse = chatModel.call(new Prompt(
            "You are a helpful assistant. Generate a context for the transaction. The transaction is: " + transaction.generateTransactionString(),
            OpenAiChatOptions.builder().model("gpt-4o-mini").build()
        ));
        return chatResponse.getResult().getOutput().getText();
    }

    public float[] Embed(String text) {
        System.out.println(System.getenv("SPRING_AI_OPENAI_API_KEY"));
        OpenAiApi openAiApi = OpenAiApi.builder()
            .apiKey(System.getenv("SPRING_AI_OPENAI_API_KEY"))
            .build();

        OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder()
                .model("text-embedding-3-small")
                .user("user-6")
                .build();

        OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, options, RetryUtils.DEFAULT_RETRY_TEMPLATE);

        float[] embedding = embeddingModel.embed(text);

        return embedding;
    }
}
