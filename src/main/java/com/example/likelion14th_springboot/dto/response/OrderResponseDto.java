package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private DeliverStatus deliverStatus;
    private String receiverName;
    private String fullAddress;
    private int totalPrice;
    private Integer remainingDeposit;
    private List<OrderItemResponseDto> orderItems;

    public static OrderResponseDto fromEntity(Orders order) {

        List<OrderItemResponseDto> items = new ArrayList<>();
        for (ProductOrders po : order.getProductOrders()) {
            items.add(OrderItemResponseDto.fromEntity(po));
        }

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .deliverStatus(order.getDeliverStatus())
                .receiverName(order.getShippingAddress().getReceiverName())
                .fullAddress(order.getShippingAddress().getFullAddress())
                .totalPrice(order.getTotalPrice())
                .remainingDeposit(order.getBuyer().getDeposit())
                .orderItems(items)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItemResponseDto {

        private Long productId;
        private String productName;
        private int orderPrice;
        private int quantity;
        private int subtotal;

        public static OrderItemResponseDto fromEntity(ProductOrders po) {
            return OrderItemResponseDto.builder()
                    .productId(po.getProduct().getId())
                    .productName(po.getProduct().getName())
                    .orderPrice(po.getOrderPrice())
                    .quantity(po.getQuantity())
                    .subtotal(po.getSubtotal())
                    .build();
        }
    }

}
