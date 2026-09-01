package com.hmall.trade.listener;

import com.hmall.trade.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayStatusListener {

    @Autowired
    private IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("trade.pay.status.queue"),
            exchange = @Exchange(value = "pay.direct",type = ExchangeTypes.DIRECT),
            key = "pay.status"
    ))
    public void makePayOrderSuccess(Long orderId){
        log.info("收到支付成功消息，orderId：{}", orderId);
        orderService.markOrderPaySuccess(orderId);
    }
}
