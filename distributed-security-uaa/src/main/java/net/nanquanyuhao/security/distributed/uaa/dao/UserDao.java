package net.nanquanyuhao.security.distributed.uaa.dao;

import net.nanquanyuhao.security.distributed.uaa.model.PermissionDto;
import net.nanquanyuhao.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问类
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据账号查询用户信息
     *
     * @param username
     * @return
     */
    public UserDto getUserByUsername(String username) {

        String sql = "select id, username, password, fullname, mobile from t_user where username = ?";
        // 连接数据库查询用户
        List<UserDto> list = jdbcTemplate.query(sql, new Object[]{username},
                new BeanPropertyRowMapper(UserDto.class));
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户 id 查询用户权限
     *
     * @param userId
     * @return
     */
    public List<String> findPermissionByUserId(String userId) {

        String sql = "SELECT\n" +
                "\t* \n" +
                "FROM\n" +
                "\tt_permission \n" +
                "WHERE\n" +
                "\tid IN ( SELECT permission_id FROM t_role_permission WHERE role_id IN " +
                "( SELECT role_id FROM t_user_role WHERE user_id = ? ) )";

        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId},
                new BeanPropertyRowMapper(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));

        return permissions;
    }
}
