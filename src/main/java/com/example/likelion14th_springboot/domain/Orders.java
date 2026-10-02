package com.example.likelion14th_springboot.domain;

import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliverStatus deliverStatus; // 배송상태

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<ProductOrders> productOrders = new ArrayList<>();

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Coupon coupon;

    @Embedded
    private ShippingAddress shippingAddress;

    @Column(nullable = false)
    private boolean deleted;

    private int totalPrice;

    @Builder
    public Orders(Member buyer, ShippingAddress shippingAddress) {
        this.buyer = buyer;
        this.shippingAddress = shippingAddress;
        this.deliverStatus = DeliverStatus.PREPARATION;
        this.deleted = false;
        this.totalPrice = 0;
    }

    public void addProductOrders(ProductOrders productOrders) {
        this.productOrders.add(productOrders);        // 거울
        productOrders.assignOrders(this);             // 주인 ← ProductOrders에 만들어야 함
        this.totalPrice += productOrders.getSubtotal(); // 총액 누적
    }

    public void updateShippingAddress(ShippingAddress newAddress) {
        if (this.deliverStatus != DeliverStatus.PREPARATION) {
            throw new IllegalArgumentException("배송 준비 중인 주문만 배송지를 수정할 수 있습니다.");
        }
        this.shippingAddress = newAddress; // 값 타입은 불변이라 통째로 교체
    }

    public void changeDeliverStatus(DeliverStatus status) {
        this.deliverStatus = status;
    }

    public void softDelete() {
        if (this.deliverStatus != DeliverStatus.COMPLETED) {
            throw new IllegalArgumentException("배송 완료된 주문만 삭제할 수 있습니다.");
        }
        this.deleted = true;
    }
}
