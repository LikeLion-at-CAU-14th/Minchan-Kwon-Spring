package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.ShippingAddress;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// dto/response/OrderDetailResponseDto.java  (상세용)
@Getter
@Builder
@AllArgsConstructor
public class OrderDetailResponseDto {
    private Long orderId;
    private DeliverStatus deliverStatus;
    private String receiverName;
    private String roadAddress;
    private String detailAddress;
    private String postalCode;
    private String phoneNumber;
    private int totalPrice;
    private LocalDateTime createdAt;
    private List<OrderResponseDto.OrderItemResponseDto> orderItems;

    public static OrderDetailResponseDto fromEntity(Orders order) {
        ShippingAddress address = order.getShippingAddress();

        List<OrderResponseDto.OrderItemResponseDto> items = new ArrayList<>();
        for (ProductOrders po : order.getProductOrders()) {
            items.add(OrderResponseDto.OrderItemResponseDto.fromEntity(po));
        }

        return OrderDetailResponseDto.builder()
                .orderId(order.getId())
                .deliverStatus(order.getDeliverStatus())
                .receiverName(address.getReceiverName())
                .roadAddress(address.getRoadAddress())
                .detailAddress(address.getDetailAddress())
                .postalCode(address.getPostalCode())
                .phoneNumber(address.getPhoneNumber())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .orderItems(items)
                .build();
    }
}