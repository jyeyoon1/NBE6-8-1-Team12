package com.caffe.global.component;

import org.springframework.stereotype.Component;

@Component
public class MockPaymentGatewayClient {

    public boolean charge(String paymentMethod, String paymentName,  String paymentInfo, double amount) {
        System.out.printf("결제 방법 : %s , 결제사 : %s, 결제 요청 번호 : %s, 결제 금액 : %f", paymentMethod, paymentName, paymentInfo, amount);
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return Math.random() < 0.95;
    }
}
