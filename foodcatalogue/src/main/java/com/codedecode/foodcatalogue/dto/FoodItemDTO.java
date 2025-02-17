package com.codedecode.foodcatalogue.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    private int id;

    private String itemName;

    private String itemDescription;

    private  boolean isVeg;

    private BigDecimal price;

    private  Integer restaurantId;
    private Integer quantity;
}
