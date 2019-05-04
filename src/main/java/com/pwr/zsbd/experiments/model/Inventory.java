package com.pwr.zsbd.experiments.model;

import lombok.Data;

@Data
public class Inventory {

    private Product product;
    private Warehouse warehouse;
    private Double quantity;

}
