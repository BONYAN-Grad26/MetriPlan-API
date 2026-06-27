package com.abdelaziz26.metriplate.dtos.store;

import com.abdelaziz26.metriplate.entities.store.Order;
import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResponse {

    private Long orderId;

    private OrderStatus status;

    public static CreateOrderResponse from(Order order) {
        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getOrderStatus())
                .build();
    }
}
