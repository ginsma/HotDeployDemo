package test.java.com.bocsoft.deploy.dao.daoimpl;

/**
 * Created by Jean on 2017/3/3.
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import main.java.com.bocsoft.deploy.service.serviceIml.MySqlMapClientImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.java.com.bocsoft.deploy.beans.Users;
import test.java.com.bocsoft.deploy.dao.UsersDao;


public class UsersDaoImpl implements UsersDao {
    private MySqlMapClientImpl sqlMapClient ;

    public List<Users> selectAllStudent() {
        // 读取配置文件
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:main/resources/applicationContext.xml");
        sqlMapClient = (MySqlMapClientImpl) ctx.getBean("sqlMapClient");
        List<Users> students = null;
        try {
            students = sqlMapClient.queryForList("selectAllStudent");
            //关键代码
            sqlMapClient.fresh();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

}