package com.abdelaziz26.metriplate.services.store;

import com.abdelaziz26.metriplate.dtos.store.CreateOrderResponse;
import com.abdelaziz26.metriplate.dtos.store.OrderResponseDto;
import com.abdelaziz26.metriplate.entities.store.CartItem;
import com.abdelaziz26.metriplate.entities.store.Order;
import com.abdelaziz26.metriplate.entities.store.OrderItem;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import com.abdelaziz26.metriplate.repositories.CartRepository;
import com.abdelaziz26.metriplate.repositories.OrderRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService{

    SecurityContextService securityContextService;
    CartRepository         cartRepository;
    OrderRepository        orderRepository;

    @Override
    public Result<CreateOrderResponse, Error> placeOrder() {

        User user = securityContextService.getCurrentUser().orElse(null);

        if (user == null)
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Login first"));

        List<CartItem> cartItems =
                cartRepository.findByUser_Id(user.getId());

        if (cartItems.isEmpty()) {
            return Result.CreateErrorResult(
                    Errors.BadRequestErr("Cart is empty."));
        }

        Order order = new Order();

        order.setUser(user);
        order.setOrderedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            OrderItem item = new OrderItem();

            item.setOrder(order);

            item.setIngredient(cartItem.getIngredient());

            item.setQuantity(cartItem.getQuantity());

            item.setUnitPrice(cartItem.getIngredient().getPrice());

            BigDecimal itemTotal =
                    cartItem.getIngredient()
                            .getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            item.setTotalPrice(itemTotal);

            total = total.add(itemTotal);

            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        return Result.CreateSuccessResult(
                CreateOrderResponse.from(order));
    }

    @Override
    public Result<List<OrderResponseDto>, Error> getMyOrders() {

        User user = securityContextService.getCurrentUser().orElse(null);

        if (user == null)
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Login first"));

        return Result.CreateSuccessResult(
                orderRepository.findByUser_Id(user.getId())
                        .stream()
                        .map(OrderResponseDto::from)
                        .toList());
    }

    @Override
    public Result<OrderResponseDto, Error> getOrder(Long orderId) {

        User user = securityContextService.getCurrentUser().orElse(null);

        if (user == null)
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Login first"));

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null)
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("Order not found"));

        if (!Objects.equals(order.getUser().getId(), user.getId()))
            return Result.CreateErrorResult(
                    Errors.ForbiddenErr("Access denied"));

        return Result.CreateSuccessResult(
                OrderResponseDto.from(order));
    }

    @Override
    public Result<Void, Error> cancelOrder(Long orderId) {
        User user = securityContextService.getCurrentUser().orElse(null);

        if (user == null)
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Login first"));

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null)
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("Order not found"));

        if (!Objects.equals(order.getUser().getId(), user.getId()))
            return Result.CreateErrorResult(
                    Errors.ForbiddenErr("Access denied"));

        if (order.getOrderStatus() != OrderStatus.PENDING)
            return Result.CreateErrorResult(
                    Errors.BadRequestErr("Order cannot be cancelled"));

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return Result.CreateSuccessResult(null);
    }

    @Override
    public Result<Void, Error> changeStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null)
            return Result.CreateErrorResult(
                    Errors.NotFoundErr("Order not found"));

        order.setOrderStatus(status);

        if (status == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        orderRepository.save(order);

        return Result.CreateSuccessResult(null);
    }
}
