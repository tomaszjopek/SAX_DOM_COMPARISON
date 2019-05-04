package com.pwr.zsbd.experiments.model;

import lombok.Data;

@Data
public class Product {

    private int id;
    private int categoryId;
    private String productName;
    private String description;
    private double price;
    private double cost;

}
