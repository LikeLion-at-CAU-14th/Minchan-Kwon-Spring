package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {

    private DeliverStatus deliverStatus;
}
