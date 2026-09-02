package com.hmall.api.feignclient;

import com.hmall.api.dto.PayOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pay-service")
public interface PayClient {

    @GetMapping("/pay-orders/biz/{bizOrderNo}")
    public PayOrderDTO queryPayOrderByBizOrderNo(@PathVariable("bizOrderNo") Long bizOrderNo);
}
