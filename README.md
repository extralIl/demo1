
使用navicat导出表结构

接口1:登录
启动服务器后,访问login.html,输入账号和密码
如果已经存入数据库的话会进入用户页面,并分配一个session里面有用户对应id和state:active,如果五分钟不对session进行操作,超时销毁
如果数据库中没有找到说明是新用户,转到欢迎页面,提示请先注册

接口2:登出
访问登出方法,如果有未过期session则销毁.如果没有未过期session就分配一个新的然后销毁:)


接口3:访问用户信息
如果session中没有id项,则重定向到登录页面
若有,则根据id去数据库中查找对应信息,并返回一个包含信息的页面


接口4:上传头像
如果session中没有id项,则重定向到登录页面
若有,则进入上传页面,选择头像进行上传.,更新session
实现是把图片存入本地的一个文件,文件名是当时的时间,把文件的路径存入数据库对应项


接口5:注册
访问Register,post中写参数
最少有account,password,name
且account不能重复,如果发现与数据库中的重复会返回提示页面


测试:

启动tomcat,配置好环境,并创建好数据库,表

注册:
http://localhost:8080/demo1_Login_war_exploded/Register?account=123&password="as"&name="ex"&age=12&sex=1&avatar=null&signature="asdf"
类型是post

登录:启动tomcat后,url后面写login.html,输入账号密码登录

登出:http://localhost:8080/demo1_Login_war_exploded/Logout

访问用户信息: 登录页面后点击访问,或者http://localhost:8080/demo1_Login_war_exploded/GetUserInfo

上传图片:登录后访问up.html







