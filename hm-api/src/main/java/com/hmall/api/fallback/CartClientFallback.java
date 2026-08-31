package com.hmall.api.fallback;

import com.hmall.api.feignclient.CartClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;

@Slf4j
public class CartClientFallback implements FallbackFactory<CartClient> {
    @Override
    public CartClient create(Throwable cause) {
        return new CartClient() {
            @Override
            public void deleteCartItemByIds(Collection<Long> ids) {
                log.error("删除购物车失败，ids：{}", ids, cause);
                //删除失败触发回滚，抛出异常
                throw new RuntimeException(cause);
            }
        };
    }
}
