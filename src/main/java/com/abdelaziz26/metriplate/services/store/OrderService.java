package com.abdelaziz26.metriplate.services.store;

import com.abdelaziz26.metriplate.dtos.store.CreateOrderResponse;
import com.abdelaziz26.metriplate.dtos.store.OrderResponseDto;
import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.Result_.Error;


import java.util.List;

public interface OrderService {
    Result<CreateOrderResponse, Error> placeOrder();

    Result<List<OrderResponseDto>, Error> getMyOrders();

    Result<OrderResponseDto, Error> getOrder(Long orderId);

    Result<Void, Error> cancelOrder(Long orderId);

    Result<Void, Error> changeStatus(Long orderId, OrderStatus status);
}
