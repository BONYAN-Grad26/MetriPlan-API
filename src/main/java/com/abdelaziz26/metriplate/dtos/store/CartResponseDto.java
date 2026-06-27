package com.abdelaziz26.metriplate.dtos.store;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CartResponseDto {

    List<CartItemResponseDto> CartItems;
}
