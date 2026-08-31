package com.hmall.api.fallback;

import com.hmall.api.feignclient.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public void deductMoney(String pw, Integer amount) {
                log.error("扣除用户余额失败，pw：{}，amount：{}", pw, amount, cause);
                // 扣除用户余额失败，触发回滚
                throw new RuntimeException(cause);
            }
        };
    }
}
