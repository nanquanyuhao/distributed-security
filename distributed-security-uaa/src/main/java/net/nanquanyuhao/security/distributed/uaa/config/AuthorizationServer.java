package net.nanquanyuhao.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务配置
 */
@Configuration
@EnableAuthorizationServer // 开启认证服务
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 将客户端信息存储至数据库
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {

        // 客户端相关信息直接使用依赖从数据库取
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 配置客户端详情信息服务，客户端信息于此处初始化
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(clientDetailsService);
        // 暂时使用内存方式
        /*clients.inMemory()
                // client_id，第三方应用的客户端 ID
                .withClient("c1")
                // 客户端密钥
                .secret(new BCryptPasswordEncoder().encode("secret"))
                // 资源列表
                .resourceIds("res1")
                // 该客户端允许的首选类型
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit",
                        "refresh_token")
                // 允许的授权范围，即第三方应用的业务作用域
                .scopes("all")
                // false 跳转到授权页面，true 的话直接发令牌
                .autoApprove(false)
                // 加上验证回调地址
                .redirectUris("http://www.baidu.com");*/
    }

    /**
     * 配置令牌访问服务，管理令牌
     * 令牌可被用来加载身份信息，里面包含了这个令牌的相关权限
     *
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {

        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        // 支持令牌刷新
        service.setSupportRefreshToken(true);
        // 设置令牌存储策略
        service.setTokenStore(tokenStore);

        // 令牌增强，即定义 JWT 令牌服务
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期 2 小时
        service.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期 3 天
        service.setRefreshTokenValiditySeconds(259200);

        return service;
    }

    /**
     * 设置授权码模式的授权码如何存取
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        // 设置授权码模式为数据库
        return new JdbcAuthorizationCodeServices(dataSource);
    }
    /*@Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 暂时采用内存方式
        return new InMemoryAuthorizationCodeServices();
    }*/

    /**
     * 令牌访问端点及令牌服务配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 认证管理器（密码模式需要）
                .authenticationManager(authenticationManager)
                // 授权码模式需要（authorization_code）
                .authorizationCodeServices(authorizationCodeServices)
                // 令牌管理服务
                .tokenServices(tokenService())
                // 允许 post 提交
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌访问端点的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // /oauth/token_key 公开
                .tokenKeyAccess("permitAll()")
                // /oauth/check_token 公开
                .checkTokenAccess("permitAll()")
                // 表单认证，申请令牌
                .allowFormAuthenticationForClients();
    }
}