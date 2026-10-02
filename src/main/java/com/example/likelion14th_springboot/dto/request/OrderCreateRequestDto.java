package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.ShippingAddress;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequestDto {

    private Long memberId;
    private ShippingAddressDto shippingAddress;
    private List<OrderItemDto> orderItems;

    @Getter
    public static class ShippingAddressDto {
        private String receiverName;
        private String phoneNumber;
        private String roadAddress;
        private String detailAddress;
        private String postalCode;

        public ShippingAddress toShippingAddress() {
            return ShippingAddress.builder()
                    .receiverName(this.receiverName)
                    .phoneNumber(this.phoneNumber)
                    .roadAddress(this.roadAddress)
                    .detailAddress(this.detailAddress)
                    .postalCode(this.postalCode)
                    .build();
        }
    }

    @Getter
    public static class OrderItemDto {
        private Long productId;
        private int quantity;
    }
}