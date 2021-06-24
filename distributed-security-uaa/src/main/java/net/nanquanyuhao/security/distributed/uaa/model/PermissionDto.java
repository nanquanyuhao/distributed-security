package net.nanquanyuhao.security.distributed.uaa.model;

import lombok.Data;

/**
 * 权限实体
 */
@Data
public class PermissionDto {

    private String id;

    private String code;

    private String description;

    private String url;
}
