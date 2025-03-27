package com.example.demo.Entity;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@EnableAutoConfiguration
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String title;
    public String description;
    public String amount;
    public String date;
    public String type;
    public String category;
    public String subCategory;

    
    public String generateTransactionString() {
        StringBuilder sb = new StringBuilder();
        sb.append(title != null ? title.toLowerCase() : "")
          .append(" ")
          .append(description != null ? description.toLowerCase() : "")
          .append(" transaction ")
          .append(type != null ? type.toLowerCase() : "")
          .append(" in category ")
          .append(category != null ? category.toLowerCase() : "")
          .append(" ")
          .append(subCategory != null ? subCategory.toLowerCase() : "")
          .append(" amount ")
          .append(amount != null ? amount : "")
          .append(" on ")
          .append(date != null ? date : "");
        
        return sb.toString().trim();
    }

}
