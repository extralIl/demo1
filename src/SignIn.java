import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/SignIn")
public class SignIn extends HttpServlet {
    //JDBC驱动名,数据库URL
    static final String JDBC_DRIVER = data.JDBC_DRIVER;
    static final String DB_URL = data.DB_URL;
    //数据库用户密码
    static final  String USER = data.USER;
    static final String PASS = data.PASS;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理中文
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");



        String title = "欢迎";
        PrintWriter out = resp.getWriter();
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n");
        //获取连接
        Connection connection =null;
        //SQL查询
        Statement statement =null;
        try {
            //注册驱动
            Class.forName(JDBC_DRIVER);

            //获取连接
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            //SQL查询
            statement = connection.createStatement();
            String sql;
            sql = "SELECT id,account,password FROM user";
            ResultSet resultSet = statement.executeQuery(sql);

            boolean isFind = false;
            String receive_account = req.getParameter("account");
            String receive_password = req.getParameter("password");
            Integer id=null;
            while (resultSet.next()) {
                //转成string
                id = resultSet.getInt("id");
                String account = resultSet.getInt("account")+"";
                String password = resultSet.getString("password");
                if(account.equals(receive_account)){
                    if(password.equals(receive_password)){
                        isFind = true;
                        out.println("账户:"+account);
                        out.println(",密码:"+password);
                        out.println("<br/>");
                        break;
                    }
                }
            }
            if(isFind==false){
                out.println("请先注册<br/>");
            }else {
                //成功登录,记录session
                HttpSession session = req.getSession();
                //设置session过期时间 5分钟,这个过期时间是从session不活动开始计算的    测试成功,是可以通过这个方法来5分钟掉线一次的,并且也确实可以通过刷新state值来刷新session来保证活跃session不掉线
                session.setMaxInactiveInterval(5*60);//5*60
                //session中添加state属性,id
                session.setAttribute("state","active");
                session.setAttribute("id",id);//Integer

                HttpSession mysession = req.getSession();
                if(mysession.getAttribute("id")!=null){//这里经过测试是可以获取到的
                    out.println("session 中的id值:"+mysession.getAttribute("id"));
                }else {
                    out.println("自己的id获取不到???");
                }

                //加上一个超链接,访问用户信息
                out.println("<br/>");
                out.println("<a href=\"http://localhost:8080/demo1_Login_war_exploded/GetUserInfo\">访问用户信息</a>");
                //加上一个超链接,登出
                out.println("<br/>");
                out.println("<a href=\"http://localhost:8080/demo1_Login_war_exploded/Logout\">登出</a>");
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
