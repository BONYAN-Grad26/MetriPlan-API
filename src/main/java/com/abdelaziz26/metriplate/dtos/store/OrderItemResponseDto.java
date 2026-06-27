package com.abdelaziz26.metriplate.dtos.store;

import com.abdelaziz26.metriplate.entities.store.OrderItem;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {

    private Long ingredientId;
    private String ingredientName;
    private String ingredientImageUrl;
    private Double quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static OrderItemResponseDto from(OrderItem item) {
        return OrderItemResponseDto.builder()
                .ingredientId(item.getIngredient().getId())
                .ingredientName(item.getIngredient().getName())
                .ingredientImageUrl(item.getIngredient().getImageUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }

}
