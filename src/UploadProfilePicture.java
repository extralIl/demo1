import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;

@WebServlet("/UploadProfilePicture")
@MultipartConfig
public class UploadProfilePicture extends HttpServlet {
    //session刷新一下


    //JDBC驱动名,数据库URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/demo1_login?useSSL=false&serverTimezone=UTC";
    //数据库用户密码
    static final  String USER = "root";
    static final String PASS = "jiweihao";


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
            //获取id
            String id = (Integer)session.getAttribute("id")+"";
            //处理中文
            resp.setContentType("text/html;charset=UTF-8");
            req.setCharacterEncoding("UTF-8");
            //获取上传的文件
            Collection<Part> list = req.getParts();
            //现在应该只有一个图片,因此准备好路径
            String path = "";

            for(Part part:list){

                //获取当前时间的字符串
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())+"";
                //创建一个文件对象,
                File temp = new File("C:\\Users\\Administrator\\Desktop",time);
                if (temp.createNewFile()) {
                    // 文件创建成功:
                    System.out.println("文件创建成功");
                    System.out.println("路径:"+temp.getAbsolutePath());
                }else {
                    //
                    System.out.println("文件创建失败");
                }


                try(InputStream in = part.getInputStream();
                    OutputStream out = new FileOutputStream(temp);
                ){
                    byte[] bytes = new byte[1000];
                    while (in.read(bytes)!=-1){
                        out.write(bytes);
                    }

                }catch (IOException e){
                    System.out.println("发生io错误");
                    e.printStackTrace();
                }

                path = temp.getAbsolutePath();


            }





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

                //因为sql 也是用\\代表\,因此要替换掉
                path = path.replaceAll("\\\\","\\\\\\\\");
                String sql = "UPDATE USER SET avatar = '"+path+"' WHERE id="+id;

                //执行更新
                int res = statement.executeUpdate(sql);
                if(res==0){
                    System.out.println("图片路径更新失败,sql语句估计写错了");
                }else {
                    System.out.println("图片路径更新成功");
                }



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






        }//else




    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
