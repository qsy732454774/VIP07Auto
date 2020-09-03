package com.testing.common;

import java.sql.*;
import java.util.*;

/**
 * 用于完成数据库处理的类。
 */
public class MysqlUtils {

    public Connection mycon;

    /**
     * 基于properties文件，创建mysql连接
     *
     * @return
     */
    public Connection createConnection() {
        Properties royp = new Properties();
        mycon = null;
        try {
            royp.load(this.getClass().getResourceAsStream("/vip07db.properties"));
            String driver = royp.getProperty("driverClass");
            String url = royp.getProperty("jdbcUrl");
            String user = royp.getProperty("mysqlu");
            String pwd = royp.getProperty("mysqlp");
            Class.forName(driver);
            mycon = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建mysql连接失败");
        }
        return mycon;
    }

    /**
     * 输入用户名密码，验证是否登录成功
     *
     * @param user
     * @param pwd
     * @return 返回布尔值。
     */
    public boolean login(String user, String pwd) {
        try {
            boolean loginResult = false;
            Statement statement = mycon.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from userinfo where username='" + user + "' and pwd='" + pwd + "'");
            if (resultSet.next()) {
                loginResult = true;
            } else {
                loginResult = false;
            }
            statement.close();
            resultSet.close();
            return loginResult;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //不管是try 还是 catch 最后都执行 finally
        finally {
            try {
                mycon.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("连接已经关闭了");
            }
        }
    }

    /**
     * 存储过程查询mysql判断登录方法
     * @param user  用户名
     * @param pwd   密码
     * @return  登录成功与否
     */
    public boolean proLogin(String user, String pwd) {
        //登录是否成功的结果
        boolean result = false;
        try {
            //建立存储过程调用对象。
            CallableStatement cst = mycon.prepareCall("{call login(?,?)}");
            //设置存储过程调用时，第1个参数是user，第2个参数是pwd
            cst.setString(1, user);
            cst.setString(2, pwd);
            //执行存储过程，得到结果集
            ResultSet rst = cst.executeQuery();
            //结果集有值说明登录成功
            if (rst != null && rst.next()) {
                System.out.println("查询到的用户名是：" + rst.getString("username"));
                result = true;
            } else {
                result = false;
            }
            //关闭查询和结果集，释放资源
            cst.close();
            rst.close();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return result;
        } finally {
            try {
                mycon.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("连接已经被关闭");
            }
        }


    }

    /**
     * 基于sql语句进行查询，处理所有的数据，返回一个list<map>
     *
     * @param sql 执行的sql语句
     * @return 返回的结果内容
     */
    public List<Map<String, String>> queryDatas(String sql) {
        try {
            boolean loginResult = false;
            Statement statement = mycon.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, String>> allData = new ArrayList<>();
            int count = 1;
            //每一行数据
            while (resultSet.next()) {
                Map<String, String> lineData = new HashMap<>();
                //metadata从1开始取index。
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    //存储表头信息每一列的列名和这一行对应的值到map里面
                    lineData.put(metaData.getColumnName(i), resultSet.getString(metaData.getColumnName(i)));
                }
                //添加map到list中
                allData.add(lineData);
                count++;
            }
            return allData;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        //不管是try 还是 catch 最后都执行 finally
        finally {
            try {
                mycon.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("连接已经关闭了");
            }
        }
    }

    /**
     * 通过用户名查询获取用户信息除用户名和密码以外的内容
     *
     * @param name
     * @return
     */
    public Map<String, String> getUserInfo(String name) {
        String sql = "SELECT * FROM userinfo where username='" + name + "';";
        try {
            // 通过数据库连接实例化statement对象
            Statement sm = mycon.createStatement();
            // 执行查询
            ResultSet rs = sm.executeQuery(sql);
            // 创建map存储表中信息
            Map<String, String> map = new HashMap<String, String>();
            //设置读取结果的循环控制变量，代表获取的数据的行数
            /* rs!=null说明sql语句执行查找成功，有内容返回。
             * rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
             * 如果sql查询不止一条语句，则可以用while循环取这些值
             */
            if (rs != null && rs.next()) {
                // 获取表头信息,通过rsmd来获取数据的列数
                ResultSetMetaData rsmd = rs.getMetaData();
                // 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    //展示的信息去除密码和用户名
                    if (!(rsmd.getColumnName(i).equals("password") || rsmd.getColumnName(i).equals("username")))
                        // 将每一列的名称和数据作为键值对存放到map当中，将行数拼接到键里
                        map.put(rsmd.getColumnName(i), rs.getString(i));
                }
                System.out.println(map.toString());
            }
            // 关闭statement对象和查询结果集合对象，释放资源
            sm.close();
            rs.close();
            return map;
        } catch (SQLException e) {
        }
        return null;
    }

}
