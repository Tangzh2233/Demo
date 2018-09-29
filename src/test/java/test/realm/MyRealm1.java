package test.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import test.exception.UserNameException;

/**
 * Created by Administrator on 2017/8/29.
 */
public class MyRealm1 implements Realm {
    @Override
    public String getName() {
        return "My Realm1";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String) token.getPrincipal();//获取用户名
        String password = new String((char[]) token.getCredentials());//获取密码
        System.out.println(username+"#####");
        System.out.println(password+"#####");
        if(!"tang".equals(username)){
            System.out.println("MyRealm1 验证失败");
            throw new UserNameException("用户名错误");
        }
        if(!"123".equals(password)){
            throw  new UserNameException("密码错误");
        }
        return new SimpleAuthenticationInfo(username,password,getName());
    }
}
