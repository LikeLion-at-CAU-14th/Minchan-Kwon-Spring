package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderSummaryResponseDto {
    private Long orderId;
    private DeliverStatus deliverStatus;
    private int totalPrice;
    private int itemCount;
    private LocalDateTime createdAt;

    public static OrderSummaryResponseDto fromEntity(Orders order) {
        // TODO: builder로 5개 필드를 채워서 return
        //  - itemCount는 order.getProductOrders()의 크기
        //  - createdAt은 BaseTimeEntity에서 물려받은 필드의 getter

        return OrderSummaryResponseDto.builder()
                .orderId(order.getId())
                .deliverStatus(order.getDeliverStatus())
                .totalPrice(order.getTotalPrice())
                .itemCount(order.getProductOrders().size())
                .createdAt(order.getCreatedAt())
                .build();
    }
}