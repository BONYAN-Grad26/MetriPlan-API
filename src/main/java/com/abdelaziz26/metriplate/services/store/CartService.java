package com.abdelaziz26.metriplate.services.store;

import com.abdelaziz26.metriplate.dtos.store.CartItemResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;

import java.util.List;

public interface CartService {
    Result<List<CartItemResponseDto>, Error> getCartItems();
    Result<Void, Error> addToCart(Long ingredientId);
    Result<Void, Error> changeQuantity(Long itemId, Double newQuantity);
    Result<Void, Error> removeFromCart(Long itemId);
}

