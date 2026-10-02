package com.example.likelion14th_springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable                                          // ← 값 타입 선언
@Getter                                              // ← 조회는 허용
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // ← JPA용, 외부 차단
public class ShippingAddress {

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String roadAddress;

    private String detailAddress;
    private String postalCode;

    @Builder
    public ShippingAddress(String receiverName, String phoneNumber, String roadAddress, String detailAddress, String postalCode) {
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
    }

    public String getFullAddress() {
        if (detailAddress == null || detailAddress.isBlank()) {
            return roadAddress;
        } else {
            return roadAddress + " " + detailAddress;
        }
    }
}