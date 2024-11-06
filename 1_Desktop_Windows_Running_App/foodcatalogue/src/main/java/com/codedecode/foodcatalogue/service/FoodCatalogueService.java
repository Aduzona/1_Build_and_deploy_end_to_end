package com.codedecode.foodcatalogue.service;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;
import com.codedecode.foodcatalogue.mapper.FoodItemMapper;
import com.codedecode.foodcatalogue.repo.FoodItemRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FoodCatalogueService {

    @Autowired
    FoodItemRepo foodItemRepo;

    @Autowired
    RestTemplate restTemplate;//LoadBalanced restTemplate

    private static final Logger logger = LoggerFactory.getLogger(FoodCatalogueService.class);

    public FoodItemDTO addFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItemSavedInDB= foodItemRepo.save(FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO));
        return  FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDto(foodItemSavedInDB);
    }

    public FoodCataloguePage fetchFoodCataloguePageDetails(Integer restaurantId) {

        // We need 2 things
        // food item list
        List<FoodItem> foodItemList = fetchFoodItemList(restaurantId);

        //restaurantdetails
        Restaurant restaurant = fetchRestaurantDetailsFromRestaurantMS(restaurantId);

        return createFoodCataloguePage(foodItemList,restaurant);


    }

    /**
     * Merge 2 responses from fetchRestaurant...() and fetchFoodItemList(..) as
     * one food catalog page
     * @param foodItemList
     * @param restaurant
     */
    private FoodCataloguePage createFoodCataloguePage(List<FoodItem> foodItemList, Restaurant restaurant) {
        FoodCataloguePage foodCataloguePage =new FoodCataloguePage();
        foodCataloguePage.setFoodItemsList(foodItemList);
        foodCataloguePage.setRestaurant(restaurant);
        return foodCataloguePage;
    }

    /**
     * Goes to the restaurantlisting microservice and get details
     * @param restaurantId
     * @return
     */
    private Restaurant fetchRestaurantDetailsFromRestaurantMS(Integer restaurantId) {
        return restTemplate.getForObject("http://RESTAURANTLISTING/restaurant/fetchById/"+restaurantId,Restaurant.class);//As seen in Eureka, RESTAURANTLISTING.  Eureka will figure this path out.

    }

    /**
     * Go hit the database and get the list of food items of a restaurant
     * @param restaurantId
     * @return
     */
    private List<FoodItem> fetchFoodItemList(Integer restaurantId) {
        List<FoodItem> foodItems = foodItemRepo.findAll();
        for (FoodItem item : foodItems) {
            logger.info("Fetched Food Item: {}", item); // Logs each food item
        }
        return foodItemRepo.findByRestaurantId(restaurantId);
    }
}
