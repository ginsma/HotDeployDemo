package test.java.com.bocsoft.deploy.dao.daoimpl;

/**
 * Created by Jean on 2017/3/3.
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import main.java.com.bocsoft.deploy.service.serviceIml.H2o3SqlMapClientImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.java.com.bocsoft.deploy.beans.Users;
import test.java.com.bocsoft.deploy.dao.UsersDao;


public class UsersDaoImpl implements UsersDao {
    private H2o3SqlMapClientImpl sqlMapClient ;

    public List<Users> selectAllStudent() {
        // 读取配置文件
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:main/resources/applicationContext.xml");
        sqlMapClient = (H2o3SqlMapClientImpl) ctx.getBean("sqlMapClient");
        List<Users> students = null;
        try {
            students = sqlMapClient.queryForList("selectAllStudent");
            sqlMapClient.fresh();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

}