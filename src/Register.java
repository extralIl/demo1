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
    //但是要对account进行查重,发现有重复的要提示用户
    //要填写的数据:account,password,name不能空其他都可空

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



        //获取连接
        Connection connection =null;
        //SQL查询
        Statement statement =null;
        //获取输出流
        PrintWriter out = resp.getWriter();

        try {


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
                    //判断account是否重复
                    //本来想要用数据库的唯一约束处理异常来做,但是不知道怎么具体的确认是违反唯一约束而不是别的错误,因此还是用搜索来做
                    if(paramName.equals("account")){
                        ResultSet res_acc = statement.executeQuery("SELECT account FROM USER WHERE account="+paramValue);
                        if(res_acc.next()){
                            System.out.println("账号重复了");
                            out.println("账号重复,请输入其他账号");
                            break;
                        }
                    }
                    out.println(paramName+":"+paramValue+",");
                    if(!paramNames.hasMoreElements()){//当前是最后一项了
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



            //完成后关闭
            statement.close();
            connection.close();



        } catch (ClassNotFoundException e ) {
            System.out.println("驱动注册失败");
            e.printStackTrace();
        }catch (SQLException e) {
            System.out.println("JDBC错误");
//            out.println("jdbc error,传入参数不对,sql语句不能执行,500");
            e.printStackTrace();
        }finally {
            //添加html尾部
            out.println("</body></html>");
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
