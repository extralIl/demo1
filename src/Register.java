import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

@WebServlet("/Register")
public class Register extends HttpServlet {
    //用户注册接口,用户点击注册进入界面,提交一系列表单并且确认没有格式错误之后存入数据库,并且转到登录界面
    //要填写的数据:account,password,name不能空其他都可空


    //JDBC驱动名,数据库URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/demo1_login?useSSL=false&serverTimezone=UTC";
    //数据库用户密码
    static final  String USER = "root";
    static final String PASS = "jiweihao";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理中文
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");



        //获取连接
        Connection connection =null;
        //SQL查询
        Statement statement =null;



        try {

            //获取输出流
            PrintWriter out = resp.getWriter();
            //制作返回html
            String title = "注册";
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
            //除了id外其他的都要写在Post中
            sql = "INSERT INTO USER VALUES (null,";
            //枚举集
            Enumeration paramNames = req.getParameterNames();
            //遍历
            while (paramNames.hasMoreElements()){
                String paramName = (String)paramNames.nextElement();
                //值只有一个
                String[] paramValues = req.getParameterValues(paramName);
                String paramValue = paramValues[0];
                if(paramValue.length()==0){
                    //有值没获取到
                    out.print(paramName+"没有值");
                    break;
                }else {

                    out.println(paramName+":"+paramValue+",");
                    if(!paramNames.hasMoreElements()){//当前是最后一项了
                        out.println(paramName);
                        sql = sql+paramValue+")";
                    }else {
                        sql = sql+paramValue+",";
                    }

                }

            }

//            到这里sql应该类似于:"INSERT INTO student VALUES (100, 'C++', 'Li', 18)";
            Integer result = statement.executeUpdate(sql);

            if(result==0){
                System.out.println("sql执行出错,没有插入");
            }else {
                out.println("您已成功注册!");
            }


            //添加html尾部
            out.println("</body></html>");
            //完成后关闭
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
