package com.example.likelion14th_springboot.controller;

import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderStatusUpdateRequestDto;
import com.example.likelion14th_springboot.dto.request.ShippingUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderDetailResponseDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.dto.response.OrderSummaryResponseDto;
import com.example.likelion14th_springboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders") // URL 공통 경로 매핑
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    // 구매자별 목록: GET /orders?memberId=2
    @GetMapping
    public ResponseEntity<List<OrderSummaryResponseDto>> getOrdersByMember(@RequestParam Long memberId) {
        return ResponseEntity.ok(orderService.getOrdersByMember(memberId));
    }

    // 단건 상세: GET /orders/1
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponseDto> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    // 배송지 수정: PUT /orders/1/shipping
    @PutMapping("/{id}/shipping")
    public ResponseEntity<OrderDetailResponseDto> updateShipping(@PathVariable Long id,
                                                                 @RequestBody ShippingUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.updateShippingAddress(id, dto));
    }

    // 배송 상태 변경(테스트용): PATCH /orders/1/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDetailResponseDto> updateStatus(@PathVariable Long id,
                                                               @RequestBody OrderStatusUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.updateDeliverStatus(id, dto));
    }

    // 주문 삭제(Soft Delete): DELETE /orders/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
