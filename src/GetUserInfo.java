import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.*;

@WebServlet("/GetUserInfo")
public class GetUserInfo extends HttpServlet {
    //JDBC驱动名,数据库URL
    static final String JDBC_DRIVER = data.JDBC_DRIVER;
    static final String DB_URL = data.DB_URL;
    //数据库用户密码
    static final  String USER = data.USER;
    static final String PASS = data.PASS;
//当session的state属性存在时,才能获取用户信息
    //而且每次调用该方法都要把session刷新一下,比如赋值一下
    //根据session的id获取信息
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取session
        HttpSession session = req.getSession();
        if(session.getAttribute("id")==null){
            //如果session过期了,就使其无效
            session.invalidate();
            //并重定向到登录页面----要写全
            resp.sendRedirect("http://localhost:8080/demo1_Login_war_exploded/login.html");
        }else {
            //刷新session状态
            session.setAttribute("state","active");

            resp.setContentType("text/html;charset=UTF-8");

            //获取id
            Integer id = (Integer)session.getAttribute("id");//integer


            //获取连接
            Connection connection =null;
            //SQL查询
            Statement statement =null;



            try {

                //获取输出流
                PrintWriter out = resp.getWriter();
                //制作返回html
                String title = "您的用户信息";
                String docType = "<!DOCTYPE html>\n";
                out.println(docType +
                        "<html>\n" +
                        "<head><title>" + title + "</title></head>\n" +
                        "<body bgcolor=\"#f0f0f0\">\n" +
                        "<h1 align=\"center\">" + title + "</h1>\n");



                //注册驱动
                Class.forName(JDBC_DRIVER);
                //获取连接
                connection = DriverManager.getConnection(DB_URL,USER,PASS);
                //SQL查询
                statement = connection.createStatement();
                String sql;
                sql = "SELECT * FROM user WHERE id = "+id;
                ResultSet resultSet = statement.executeQuery(sql);


                while (resultSet.next()){
                    //读取结果集
                    String account = resultSet.getInt("account")+"";
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    String age = resultSet.getInt("age")+"";
                    String sex = resultSet.getInt("sex")==1?"男":"女";
                    String avatar = resultSet.getString("avatar");
                    String signature = resultSet.getString("signature");

                    //制作输出内容
                    out.println("account:"+account);
                    out.println(",password:"+password);
                    out.println(",name:"+name);
                    out.println(",age:"+age);
                    out.println(",sex:"+sex);
                    out.println(",avatar路径:"+avatar);
                    out.println(",signature:"+signature);
                    out.println("<br/>");
                }
                out.println("</body></html>");
                //完成后关闭
                resultSet.close();
                statement.close();
                connection.close();



            } catch (ClassNotFoundException e ) {
                System.out.println("驱动注册失败");
                e.printStackTrace();
            }catch (SQLException e) {
                System.out.println("JDBC错误");
                e.printStackTrace();
            }finally {
                // 最后是用于关闭资源的块
                try{
                    if(statement!=null)
                        statement.close();
                }catch(SQLException se2){
                    System.out.println("关闭Connection出错");
                }
                try{
                    if(connection!=null)
                        connection.close();
                }catch(SQLException se){
                    System.out.println("关闭statement出错");
                    se.printStackTrace();
                }
            }
        }






    }


}
