package com.example.uberbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    @JsonProperty("price")
    private double price;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("method")
    private String method;
    @JsonProperty("intent")
    private String intent;
    @JsonProperty("description")
    private String description;

}