package com.hmall.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

// Feign 配置类不加 @Configuration，是为了隔离，防止Bean冲突；Feign 会自己管理这些配置类的加载。
// 如果配置类里只有Feign 专用且不冲突的 Bean 就没问题
public class FeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userInfo = UserContext.getUser();
                if (userInfo == null){
                    return;
                }
                // 添加用户信息到请求头，传递给下游微服务
                requestTemplate.header("user-info", userInfo.toString());
            }
        };
    }
}
