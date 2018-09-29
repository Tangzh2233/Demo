package test.web;

import com.edu.Application;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LoginTest{

    @Resource
    private ILoginService iLoginService;

    @Test
    public void Login(){
        User login = iLoginService.login("tang",2.0);
        System.out.println(login.toString());
    }
}
