package net.nanquanyuhao.security.distributed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 **/
@RunWith(SpringRunner.class)
public class TestBCrypt {

    @Test
    public void testBCrypt(){

        //对密码进行加密
        String hashpw = BCrypt.hashpw("secret", BCrypt.gensalt());
        System.out.println(hashpw);
    }
}
