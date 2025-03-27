package com.example.demo.Controller;

import io.pinecone.clients.Pinecone;


public class PineconeController {
    private Pinecone pinecone;

    public PineconeController() {
        this.pinecone = new Pinecone.Builder("pcsk_7HBiuF_B75h9iybNCFZFZqVLqHocRGEHdxCWhHxKkz24TCzkKkJY3AvPuVbjT9dQiriDh").build();
    }

    public Pinecone getPinecone() {
        return pinecone;
    }
}


