package src.com.timvan.memexsql;

/**
 * @author TimVan
 * @date 2018/5/30 21:35
 */


/**
 * 对jdbc的完整封装
 *
 */

import java.sql.*;

public class JDBCUtil {

    private static  String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static  String DB_URL ="jdbc:mysql://120.79.210.170:3306/memex?serverTimezone=UTC";
    private static  String USER = "memexuser";
    private static  String PASS = "memexuser";

    //获取模板图片的信息
    public static Object[][] getPicsInfo() {
        Connection conn = null;
        Statement stmt = null;
        Object[][] obj = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();


            String sql;
            sql = "SELECT count(id) as num FROM memepics";
            ResultSet rs = stmt.executeQuery(sql);
            int num = 0;
            while(rs.next()){
                num =  rs.getInt("num");
            }
            System.out.println("num = "+num);
            obj = new Object[num][6];
            rs.close();


            sql = "SELECT id,times, name, url,author,preview " +
                    "FROM memepics  " +
                    "ORDER BY times DESC ";
            rs = stmt.executeQuery(sql);

            int cnt = 0;
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id = rs.getInt("id");
                String id_string = String.format("%03d", id);
                int times  = rs.getInt("times");
                String name = rs.getString("name");
                String url = rs.getString("url");
                String author = rs.getString("author");
                String preview = rs.getString("preview");

                obj[cnt][0] = id_string;
                obj[cnt][1] = name;
                obj[cnt][2] = url;
                obj[cnt][3] = times;
                obj[cnt][4] = author;
                obj[cnt][5] = preview;


                cnt++;

                /// 输出数据
//                System.out.print("times: " + times);
//                System.out.print(", 站点名称: " + name);
//                System.out.print(", 站点 URL: " + url);
//                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源

            try{
                if(stmt!=null){
                    stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("关闭数据库");
        return obj;

    }

    //更新下载量
    public static void updatePicsTimes(int id) {
        Connection conn = null;
        Statement stmt = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "update memepics set times = times+1 " +
                    "WHERE id = "+id;

            //或者用PreparedStatement方法
            Statement stmt1 = conn.createStatement();
            //执行sql语句
            stmt1.executeUpdate(sql);


            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源

            try{
                if(stmt!=null){
                    stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("关闭数据库");
    }



    public static void main(String[] args){

    }



}

