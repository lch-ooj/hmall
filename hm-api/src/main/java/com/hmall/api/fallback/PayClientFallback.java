package com.hmall.api.fallback;

import com.hmall.api.dto.PayOrderDTO;
import com.hmall.api.feignclient.PayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class PayClientFallback implements FallbackFactory<PayClient> {
    @Override
    public PayClient create(Throwable cause) {
        return new PayClient() {
            @Override
            public PayOrderDTO queryPayOrderByBizOrderNo(Long bizOrderNo) {
                log.error("查询支付单失败，订单编号: {}", bizOrderNo, cause);
                return null;
            }
        };
    }
}
