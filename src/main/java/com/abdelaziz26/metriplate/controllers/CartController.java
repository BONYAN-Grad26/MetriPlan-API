package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.store.CartItemResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.store.CartService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController extends _Abdel3zizController {

    CartService cartService;

    @GetMapping
    public ResponseEntity<@NotNull Result<List<CartItemResponseDto>, Error>> getCartItems() {
        Result<List<CartItemResponseDto>, Error> result = cartService.getCartItems();
        return ResponseEntity.status(resolveStatus(result)).body(result);
    }

    @PostMapping("/{ingredientId}")
    public ResponseEntity<@NotNull Result<Void, Error>> addToCart(
            @PathVariable Long ingredientId) {

        Result<Void, Error> result = cartService.addToCart(ingredientId);
        return ResponseEntity.status(resolveStatus(result)).body(result);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<@NotNull Result<Void, Error>> changeQuantity(
            @PathVariable Long itemId,
            @RequestParam Double quantity) {

        Result<Void, Error> result = cartService.changeQuantity(itemId, quantity);
        return ResponseEntity.status(resolveStatus(result)).body(result);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<@NotNull Result<Void, Error>> removeFromCart(
            @PathVariable Long itemId) {

        Result<Void, Error> result = cartService.removeFromCart(itemId);
        return ResponseEntity.status(resolveStatus(result)).body(result);
    }
}
