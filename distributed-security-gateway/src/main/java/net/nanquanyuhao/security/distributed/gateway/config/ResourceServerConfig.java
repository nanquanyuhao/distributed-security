package net.nanquanyuhao.security.distributed.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 用于定义资源服务配置，描述某个接入客户端需要什么样的权限才能访问某个微服务
 */
@Configuration
public class ResourceServerConfig {

    /**
     * 客户端系统标识
     */
    public static final String RESOURCE_ID = "res1";

    /**
     * uaa 资源服务配置
     */
    @Configuration
    @EnableResourceServer
    public class UAAServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        /**
         * 让资源服务器从 tokenStore 中拿到 token
         *
         * @param resources
         */
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {

            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/uaa/**").permitAll();
        }
    }

    /**
     * order 资源服务配置
     */
    @Configuration
    @EnableResourceServer
    public class OrderServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        /**
         * 让资源服务器从 tokenStore 中拿到 token
         *
         * @param resources
         */
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {

            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    // 全部请求均需要客户端令牌具备 ROLE_API 权限，否则会报 insufficient_scope
                    .antMatchers("/order/**").access("#oauth2.hasScope('ROLE_API')");
        }
    }

    // 配置其他的资源服务
}
