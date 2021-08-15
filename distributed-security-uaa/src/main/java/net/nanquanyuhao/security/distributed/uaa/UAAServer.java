package net.nanquanyuhao.security.distributed.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author nanquanyuhao
 * @version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix // 开启熔断限流相关功能
@EnableFeignClients(basePackages = {"net.nanquanyuhao.security.distributed.uaa"})
public class UAAServer {

    public static void main(String[] args) {
        SpringApplication.run(UAAServer.class, args);
    }
}
