package test.java.com.bocsoft.deploy.dao;

/**
 * Created by Jean on 2017/3/3.
 */
import test.java.com.bocsoft.deploy.beans.Users;

import java.util.List;

/**
 * @author xudongwang 2011-12-31
 *
 * Email:xdwangiflytek@gmail.com
 *
 */

public interface UsersDao {

    /**
     * 查询全部学生信息
     *
     * @return 返回学生列表
     */
    public List<Users> selectAllStudent();

}