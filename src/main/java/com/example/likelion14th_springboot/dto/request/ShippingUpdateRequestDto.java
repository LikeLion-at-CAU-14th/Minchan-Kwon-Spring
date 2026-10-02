package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.ShippingAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Jackson이 JSON -> 객체로 바꿀 때 기본 생성자가 필요
public class ShippingUpdateRequestDto {

    private String receiverName;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String postalCode;

    public ShippingAddress toShippingAddress() {
        if (isBlank(receiverName) || isBlank(phoneNumber) || isBlank(roadAddress)) {
            throw new IllegalArgumentException("수령인, 전화번호, 도로명주소는 필수입니다.");
        }

        return ShippingAddress.builder()
                .receiverName(this.receiverName)
                .phoneNumber(this.phoneNumber)
                .roadAddress(this.roadAddress)
                .detailAddress(this.detailAddress)
                .postalCode(this.postalCode)
                .build();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
