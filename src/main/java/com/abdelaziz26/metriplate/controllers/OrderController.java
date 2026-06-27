package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.store.ChangeOrderStatusRequest;
import com.abdelaziz26.metriplate.dtos.store.CreateOrderResponse;
import com.abdelaziz26.metriplate.dtos.store.OrderResponseDto;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.services.store.OrderService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController extends _Abdel3zizController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<@NotNull Result<CreateOrderResponse, Error>> placeOrder() {

        Result<CreateOrderResponse, Error> result = orderService.placeOrder();

        return ResponseEntity
                .status(resolveStatus(result))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<@NotNull Result<List<OrderResponseDto>, Error>> getMyOrders() {

        Result<List<OrderResponseDto>, Error> result =
                orderService.getMyOrders();

        return ResponseEntity
                .status(resolveStatus(result))
                .body(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<@NotNull Result<OrderResponseDto, Error>> getOrder(
            @PathVariable Long orderId) {

        Result<OrderResponseDto, Error> result =
                orderService.getOrder(orderId);

        return ResponseEntity
                .status(resolveStatus(result))
                .body(result);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<@NotNull Result<Void, Error>> cancelOrder(
            @PathVariable Long orderId) {

        Result<Void, Error> result =
                orderService.cancelOrder(orderId);

        return ResponseEntity
                .status(resolveStatus(result))
                .body(result);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<@NotNull Result<Void, Error>> changeStatus(
            @PathVariable Long orderId,
            @RequestBody ChangeOrderStatusRequest request) {

        Result<Void, Error> result =
                orderService.changeStatus(orderId, request.getStatus());

        return ResponseEntity
                .status(resolveStatus(result))
                .body(result);
    }
}
