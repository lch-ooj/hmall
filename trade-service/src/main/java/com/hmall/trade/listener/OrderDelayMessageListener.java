package com.hmall.trade.listener;

import com.hmall.api.dto.PayOrderDTO;
import com.hmall.api.feignclient.PayClient;
import com.hmall.trade.constants.MQConstants;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderDelayMessageListener {

    @Autowired
    private PayClient payClient;

    @Autowired
    private IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.DELAY_ORDER_QUEUE_NAME),
            exchange = @Exchange(value = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ORDER_KEY
    ))
    public void queryOrderStauts(Long bizOrderNo) {
        log.info("收到延迟队列消息：{}", bizOrderNo);
        Order order = orderService.getById(bizOrderNo);
        if (order != null && order.getStatus() != 1){
            // 订单不存在或已支付
            return;
        }
        // 查询交易单
        PayOrderDTO payOrderDTO = payClient.queryPayOrderByBizOrderNo(bizOrderNo);
        if (payOrderDTO != null && payOrderDTO.getStatus() == 3){
            //已支付，修改订单状态
            orderService.markOrderPaySuccess(bizOrderNo);
        }else {
            log.info("订单{}超时未支付，系统自动取消", bizOrderNo);
            orderService.cancelOrder(bizOrderNo);
        }
    }
}
