package net.nanquanyuhao.security.distributed.uaa.service;

import net.nanquanyuhao.security.distributed.uaa.dao.UserDao;
import net.nanquanyuhao.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定已用户信息服务实现类
 */
@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据藏哈后查询用户信息
     *
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        // 将来连接数据库根据账号查询用户信息
        UserDto userDto = userDao.getUserByUsername(s);
        if (userDto == null) {
            // 如果用户查不到，返回 null，由 provider 来抛出异常
            return null;
        }

        // 根绝用户的 id 查询用户的权限
        List<String> permissions = userDao.findPermissionByUserId(userDto.getId());
        // 将 permissions 转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        UserDetails userDetails = User.withUsername(userDto.getUsername())
                .password(userDto.getPassword()).authorities(permissionArray).build();

        return userDetails;
    }
}
