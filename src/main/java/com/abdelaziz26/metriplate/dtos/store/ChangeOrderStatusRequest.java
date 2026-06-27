package com.abdelaziz26.metriplate.dtos.store;

import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusRequest {
    private OrderStatus status;
}
