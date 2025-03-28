package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Controller.PineconeController;
import com.example.demo.Entity.TransactionEntity;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import io.pinecone.unsigned_indices_model.ScoredVectorWithUnsignedIndices;

@Service
public class PineconeService {
    private final PineconeController pineconeHandler;
    private final OpenAIService openAIService;

    public static String INDEX_NAME = "fin-track";


    public PineconeService() {
        this.pineconeHandler = new PineconeController();
        this.openAIService = new OpenAIService();
    }

    public Pinecone getPinecone() {
        return pineconeHandler.getPinecone();
    }



    public boolean Upsert(TransactionEntity transaction) {
        try {
            // Get the Pinecone index
            Index index = getPinecone().getIndexConnection(INDEX_NAME);
            

            String context = openAIService.getContextOfTransaction(transaction);

            System.out.println("Context: " + context + " " + transaction.generateTransactionString());

            float[] embedding = openAIService.Embed(context + " " + transaction.generateTransactionString());

            List<Float> embeddingList = new ArrayList<>();
            for (float f : embedding) {
                embeddingList.add(f);
            }

            Struct metadata = Struct.newBuilder()
                .putFields("title", Value.newBuilder().setStringValue(transaction.title).build())
                .putFields("description", Value.newBuilder().setStringValue(transaction.description).build())
                .putFields("amount", Value.newBuilder().setStringValue(transaction.amount).build())
                .putFields("date", Value.newBuilder().setStringValue(transaction.date).build())
                .putFields("type", Value.newBuilder().setStringValue(transaction.type).build())
                .putFields("category", Value.newBuilder().setStringValue(transaction.category).build())
                .putFields("subCategory", Value.newBuilder().setStringValue(transaction.subCategory).build())
                .build();

                

            index.upsert("transaction-" + transaction.id.toString(), embeddingList, null, null, metadata, "ns1");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Update(TransactionEntity transaction) {
        try {
            Index index = getPinecone().getIndexConnection(INDEX_NAME);
            index.delete(Arrays.asList("transaction-" + transaction.id.toString()), false, "ns1", null);

            
            return Upsert(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> Search(String query) {
        Index index = getPinecone().getIndexConnection(INDEX_NAME);
        List<String> ids = new ArrayList<>();

        float[] embedding = openAIService.Embed(query);

        List<Float> embeddingList = new ArrayList<>();
        for (float f : embedding) {
            embeddingList.add(f);
        }

        QueryResponseWithUnsignedIndices queryResponse = index.query(3, embeddingList, null, null, null, "ns1", null, false, false);

        List<ScoredVectorWithUnsignedIndices> matches = queryResponse.getMatchesList();

                if (matches.isEmpty()) {
                        System.out.println("No matches found.");
                } else {
                        for (ScoredVectorWithUnsignedIndices match : matches) {
                                String id = match.getId();
                                System.out.println(id);
                                ids.add(id);
                                System.out.println(match.getScore());

                        }
                }

                return ids;
        }

    public boolean Delete(Integer id) {
        Index index = getPinecone().getIndexConnection(INDEX_NAME);
        index.delete(Arrays.asList("transaction-" + id.toString()), false, "ns1", null);
        return true;
    }

    public OpenAIService getOpenAIService() {
        return openAIService;
    }
}
