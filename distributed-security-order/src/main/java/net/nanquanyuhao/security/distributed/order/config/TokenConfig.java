package net.nanquanyuhao.security.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 令牌相关配置类
 */
@Configuration
public class TokenConfig {

    private String SIGNING_KEY = "uaa123";

    /*@Bean
    public TokenStore tokenStore() {
        // 内存方式，生成普通令牌
        return new InMemoryTokenStore();
    }*/
    @Bean
    public TokenStore tokenStore() {
        // JWT 令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 对称密钥，资源服务器使用该密钥来验证，为对称加密
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
        return jwtAccessTokenConverter;
    }
}
