package net.nanquanyuhao.security.distributed.uaa.model;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 **/
@Data
public class UserDto {

    public static final String SESSION_USER_KEY = "_user";

    //用户身份信息
    private String id;

    private String username;

    private String password;

    private String fullname;

    private String mobile;
}
