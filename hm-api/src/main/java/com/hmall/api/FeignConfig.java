package com.hmall.api;

import feign.Logger;
import org.springframework.context.annotation.Bean;

// Feign 配置类不加 @Configuration，是为了隔离，防止Bean冲突；Feign 会自己管理这些配置类的加载。
// 如果配置类里只有Feign 专用且不冲突的 Bean 就没问题
public class FeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }
}
