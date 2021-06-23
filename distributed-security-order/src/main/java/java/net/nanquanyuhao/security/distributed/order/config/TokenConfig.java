package java.net.nanquanyuhao.security.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 令牌相关配置类
 */
@Configuration
public class TokenConfig {

    @Bean
    public TokenStore tokenStore() {
        // 内存方式，生成普通令牌
        return new InMemoryTokenStore();
    }
}
