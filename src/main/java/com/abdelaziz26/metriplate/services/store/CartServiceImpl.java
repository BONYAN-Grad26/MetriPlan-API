package com.abdelaziz26.metriplate.services.store;

import com.abdelaziz26.metriplate.dtos.store.CartItemResponseDto;
import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.entities.store.CartItem;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.CartRepository;
import com.abdelaziz26.metriplate.repositories.IngredientRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartRepository         cartRepository;
    IngredientRepository   ingredientRepository;
    SecurityContextService securityContextService;

    public Result<List<CartItemResponseDto>, Error> getCartItems() {
        User user = securityContextService.getCurrentUser().orElse(null);
        if(user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User Not Authorized, Login first"));
        }

        return Result.CreateSuccessResult(cartRepository.findByUser_Id(user.getId()).stream().map(CartItemResponseDto::from).toList());
    }

    public Result<Void, Error> addToCart(Long ingredientId) {
        User user = securityContextService.getCurrentUser().orElse(null);
        if(user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User Not Authorized, Login first"));
        }

        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        if(ingredient == null) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Ingredient Not Found"));
        }

        cartRepository.save(CartItem.from(user, ingredient, 1D));

        return Result.CreateSuccessResult(null);
    }

    public Result<Void, Error> changeQuantity(Long itemId, Double newQuantity) {
        Result<CartItem, Error> validationResult = getOwnedCartItem(itemId);
        if(!validationResult.isSuccess()) {
            return Result.CreateErrorResult(validationResult.getError());
        }

        if (newQuantity == null || newQuantity <= 0) {
            return Result.CreateErrorResult(
                    Errors.BadRequestErr("Quantity must be greater than zero"));
        }

        CartItem cartItem = validationResult.getData();
        cartItem.setQuantity(newQuantity);
        cartRepository.save(cartItem);

        return Result.CreateSuccessResult(null);
    }

    public Result<Void, Error> removeFromCart(Long itemId) {
        Result<CartItem, Error> validationResult = getOwnedCartItem(itemId);
        if (!validationResult.isSuccess()) {
            return Result.CreateErrorResult(validationResult.getError());
        }

        cartRepository.delete(validationResult.getData());

        return Result.CreateSuccessResult(null);
    }

    private Result<CartItem, Error> getOwnedCartItem(Long itemId) {
        User user = securityContextService.getCurrentUser().orElse(null);
        if(user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("User Not Authorized, Login first"));
        }

        CartItem cartItem = cartRepository.findById(itemId).orElse(null);

        if (cartItem == null) {
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("No cart item associated with this ID."));
        }

        if (!Objects.equals(cartItem.getUser().getId(), user.getId())) {
            return Result.CreateErrorResult(
                    Errors.ForbiddenErr("You are not authorized to modify this cart item."));
        }

        return Result.CreateSuccessResult(cartItem);
    }
}
