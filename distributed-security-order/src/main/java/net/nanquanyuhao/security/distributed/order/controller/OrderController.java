package net.nanquanyuhao.security.distributed.order.controller;

import net.nanquanyuhao.security.distributed.order.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器类
 */
@RestController
public class OrderController {

    /**
     * 测试入口方法
     *
     * @return
     */
    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p2')") // 拥有 p1 权限才能访问
    public String r1() {

        // 获取用户身份信息
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDTO.getUsername() + "访问资源 1";
    }
}
