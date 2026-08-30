package com.hmall.gateway.filters;

import com.hmall.common.utils.UserContext;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private AuthProperties authProperties;

    /**
     * 路径模式匹配
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是否需要拦截
        if (isExclude(request.getPath().toString())){
            return chain.filter(exchange);
        }
        // 3.获取请求头中的token
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("authorization");
        // 4.校验token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        }catch (Exception e){
            // token 无效，设置响应状态码401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //todo 5.传递用户信息
        System.out.println("token 校验通过，用户 ID：" + userId);
//        UserContext.setUser(userId);

        return null;
    }

    /**
     * 判断请求路径是否需要拦截
     * @param path
     * @return
     */
    private boolean isExclude(String path){
        for (String excludePath : authProperties.getExcludePaths()){
            if (antPathMatcher.match(excludePath, path)){
                return true;
            }
        }
        return false;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
