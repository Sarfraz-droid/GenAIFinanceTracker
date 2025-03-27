package com.example.demo.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.TransactionEntity;
import com.example.demo.Repository.TransactionRepository;
import com.example.demo.Service.PineconeService;


@RestController
@RequestMapping("/transactions")
public class TransactionRoute {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PineconeService pineconeService;

    @GetMapping
    public List<TransactionEntity> getTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<TransactionEntity> getTransactionById(@PathVariable Integer id) {
        return transactionRepository.findById(id);
    }

    @PostMapping
    public TransactionEntity createTransaction(@RequestBody TransactionEntity transaction) {

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        pineconeService.Upsert(savedTransaction);
        return savedTransaction;
    }

    @PostMapping("/{id}")
    public String updateTransaction(@RequestBody TransactionEntity transaction, @PathVariable Integer id) {
        if (transactionRepository.findById(id).isPresent()) {
            transactionRepository.deleteById(id);
            transactionRepository.save(transaction);
            pineconeService.Update(transaction);
            return "Transaction updated";
        }
        return "Transaction not found";
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Integer id) {
        transactionRepository.deleteById(id);
        pineconeService.Delete(id);
        return "Transaction deleted";
    }

    @GetMapping("/search")
    
    public List<TransactionEntity> searchTransactions(@RequestParam String query) {

        try{
        List<String> similarIds = pineconeService.Search(query);
        List<TransactionEntity> similarTransactions = new ArrayList<TransactionEntity>();
        for (String id : similarIds) {
                String idWithoutTransaction = id.replace("transaction-", "");
                System.out.println("ID: " + idWithoutTransaction);
                TransactionEntity transaction = transactionRepository.findById(Integer.valueOf(idWithoutTransaction)).orElse(null);
                System.out.println("Transaction: " + transaction);
                if (transaction != null) {
                    similarTransactions.add(transaction);
                }
            }
            return similarTransactions;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TransactionEntity>();
        }
    }
}
