package com.caffe.domain.shipping;

import com.caffe.domain.shipping.constant.ShippingStatus;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.repository.ShippingRepository;
import com.caffe.domain.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class ShippingScheduler {

    private final ShippingRepository shippingRepository;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateShippingStatusForMorning() {
        log.info("🔔 오전 9시: 배송 상태 업데이트 시작");

        List<Shipping> shippings = shippingRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Shipping shipping : shippings) {
            if (shipping.getStatus() == ShippingStatus.BEFORE_DELIVERY) {
                shipping.updateStatus(ShippingStatus.DELIVERING, now);
                log.info("업데이트된 배송 ID: {}, 상태: {}", shipping.getId(), shipping.getStatus());
            }
        }
    }
}