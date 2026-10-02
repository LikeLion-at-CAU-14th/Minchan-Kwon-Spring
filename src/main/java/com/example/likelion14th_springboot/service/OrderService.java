package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.Product;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto.OrderItemDto;
import com.example.likelion14th_springboot.dto.request.OrderStatusUpdateRequestDto;
import com.example.likelion14th_springboot.dto.request.ShippingUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderDetailResponseDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.dto.response.OrderSummaryResponseDto;
import com.example.likelion14th_springboot.repository.MemberRepository;
import com.example.likelion14th_springboot.repository.OrderRepository;
import com.example.likelion14th_springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto dto) {

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매자입니다."));

        if (dto.getOrderItems() == null || dto.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 없습니다.");
        }

        if (dto.getShippingAddress() == null) {
            throw new IllegalArgumentException("배송정보는 필수입니다.");
        }

        Orders order = Orders.builder()
                .buyer(member)
                .shippingAddress(dto.getShippingAddress().toShippingAddress())
                .build();

        for (OrderItemDto item : dto.getOrderItems()) {
            if (item.getQuantity() < 1) {
                throw new IllegalArgumentException("1개 이상 부터 주문이 가능합니다.");
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. id =" + item.getProductId()));

            if (!product.hasEnoughStock(item.getQuantity())) {
                throw new IllegalArgumentException("재고가 부족합니다. 상품: " + product.getName() + " (요청 " + item.getQuantity() + "개 / 재고 " + product.getStock() + ")");
            }

            ProductOrders productOrders = ProductOrders.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .build();

            order.addProductOrders(productOrders);

        }

        if (!member.hasEnoughDeposit(order.getTotalPrice())) {
            throw new IllegalArgumentException("잔액이 부족합니다. 필요: " + order.getTotalPrice() + "원 / 보유: " + member.getDeposit() + "원");
        }

        for (ProductOrders po : order.getProductOrders()) {
            po.getProduct().reduceStock(po.getQuantity());
        }

        member.useDeposit(order.getTotalPrice());

        Orders saved = orderRepository.save(order);
        return OrderResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResponseDto> getOrdersByMember(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매자입니다."));

        List<Orders> orders = orderRepository.findByBuyerIdAndDeletedFalse(memberId);

        List<OrderSummaryResponseDto> items = new ArrayList<>();

        for (Orders order : orders) {
            items.add(OrderSummaryResponseDto.fromEntity(order));
        }

        return items;
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }

        return OrderDetailResponseDto.fromEntity(order);
    }

    @Transactional
    public OrderDetailResponseDto updateShippingAddress(Long orderId, ShippingUpdateRequestDto dto) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }

        order.updateShippingAddress(dto.toShippingAddress());

        return OrderDetailResponseDto.fromEntity(order);
    }

    @Transactional
    public OrderDetailResponseDto updateDeliverStatus(Long orderId, OrderStatusUpdateRequestDto dto) {
        if (dto.getDeliverStatus() == null) {
            throw new IllegalArgumentException("변경할 배송 상태는 필수입니다.");
        }

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }

        order.changeDeliverStatus(dto.getDeliverStatus());

        return OrderDetailResponseDto.fromEntity(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }

        // COMPLETED 검사는 엔티티 안에서 수행. save()/delete() 없이 변경 감지로 UPDATE 된다.
        order.softDelete();
    }
}
