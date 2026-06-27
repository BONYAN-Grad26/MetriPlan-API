package com.abdelaziz26.metriplate.dtos.store;

import com.abdelaziz26.metriplate.entities.store.Order;
import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private List<OrderItemResponseDto> items;

    private BigDecimal totalPrice;

    private LocalDateTime orderedAt;

    private LocalDateTime deliveredAt;

    private OrderStatus orderStatus;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .items(order.getOrderItems()
                        .stream()
                        .map(OrderItemResponseDto::from)
                        .toList())
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getOrderedAt())
                .deliveredAt(order.getDeliveredAt())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
