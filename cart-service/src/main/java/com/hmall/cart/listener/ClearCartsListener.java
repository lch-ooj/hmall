package com.hmall.cart.listener;

import com.hmall.cart.service.ICartService;
import com.hmall.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class ClearCartsListener {

    @Autowired
    private ICartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "cart.clear.queue"),
            exchange = @Exchange(value = "trade.topic",type = ExchangeTypes.TOPIC),
            key = {"order.create"}
    ))
    public void clearCarts(Collection<Long> itemIds, @Header("user-info")String userId) {
        log.info("清理购物车，用户ID: {}, 商品ID列表: {}", userId, itemIds);
        UserContext.setUser(Long.valueOf(userId));
        cartService.removeByItemIds(itemIds);
    }
}
