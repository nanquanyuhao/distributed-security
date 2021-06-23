package java.net.nanquanyuhao.security.distributed.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Spring Security 配置类
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 安全拦截机制（最重要）
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/r/r1").hasAuthority("p1")
                //.antMatchers("/r/r2").hasAuthority("p2")
                // 所有 /r/** 的请求必须认证通过
                .antMatchers("/r/**").authenticated()
                .anyRequest().permitAll() // 除了 /r/** ，其他的请求可以访问
                .and()
                // 允许表单登录
                .formLogin()
                // 登录页面
                .loginPage("/login-view")
                .loginProcessingUrl("/login")
                // 自定义登录成功的页面地址
                .successForwardUrl("/login-success")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-view?logout");
    }
}
