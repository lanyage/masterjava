1.什么是servlet?
servlet是处理网络请求的一套规范,所有实现servlet的类,都必须实现init,getServletConfig,service,getServletInfo,destroy方法,其中init
和destroy是生命周期方法,service是处理请求的方法。
2.servlet的实现类就能处理网络请求了吗？
不能,servlet不会直接和客户端打交道,tomcat等web容器才是直接和客户端打交道的家伙,它监听了端口,请求过来之后,根据Url信息,确定要将请求交给哪
一个servlet去处理,然后调用servlet的service方法,service方法会返回一个response对象,tomcat再把这个response返回给客户端.
3.tomcat具体工作方式?
一,tomcat将http请求文本接受并解析,并且封装成HttpServletRequest类型的request对象,所有的http头数据都可以通过request获取。
二,tomcat同时将响应的信息封装成为HttpServletResponse类型的response对象,通过设置response属性就可以控制要输出到浏览器的内容,tomcat就
将其变成响应文本的格式发送给浏览器。
4.手动编写Servlet
一,class MyServlet extends HttpServlet
二,在web.xml中配置servlet,以便tomcat根据浏览器的请求找到对应的servlet
<servlet>
    <servlet-name></servlet>
    <servlet-class></servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name/>
    <url-pattern/>
</servlet-mapping>
5.servlet的生命周期?
当客户端请求servlet的时候就会初始化一个servlet对象,也就是会执行初始化方法,然后该servlet对处理客户端的请求,在service方法中执行
最后服务器关闭时,才会销毁这个对象,执行destroy方法。
HttpServlet->GenericServlet->Servlet,ServletConfig
ServletConfig:getServletName(),getServletContext(),getInitParameter(String),getInitParameterNames();
其中servlet上下文对象封装了大部分我们需要的信息,比如说servlet的路径
6.几个重点的对象?
ServletConfig,根据web.xml创建的类,getServletName()对应的是servlet标签里面的servlet,getInitParameter(String)也是获取init-param
标签里面的参数,getInitParameterNames()是获取所有的参数,返回的是枚举类型.

ServletContext,上下文对象,tomcat为每个web项目都创建了一个上下文实例.getAttribute(String)setAttribute(String)removeAttribute()
这些方法用于为整个web项目提供全局的共享数据,getInitParameter(String)getInitParameterNames(),web.xml中初始化参数。
getRealPath("/WEB-INF/web.xml")指定xml文件的绝对路径,getResourceAsStream()和getResourcePaths("/WEB-INF")返回路径下的内容

ServletRequest,包含请求行(请求方式,资源路径,协议),请求头(当从一个页面A通过a访问另外一个页面B,在B页面有一个标记reference记录A页面的路径
,如果直接访问B没有经过A则reference为空),请求体(post请求参数存放的位置)
getRequestURI()获取统一标记符
getRequestUrl()获取统一资源定位符
getProtocol()获取协议和版本
getServerName()获取主机名
getServerPort()获取端口号
getContextPath()获取上下文路径
getServletPath()获取servlet路径
getQueryString()获取参数字符串
getRemoteAddr()获取远程地址
getParameter(String)获取请求参数
getParameterValues(String)获取select或者checkbox请求参数
getParameterMap()获取所有的请求参数

ServletResponse，包含响应行(协议,状态码200 302页面跳转 304读取页面缓存 404页面不存在 500服务器异常,对象描述信息),响应头(Content-
Encoding数据压缩,Content-Type:text/html;charset=GB2312,Content-Disposition:attachment;filename=aaa.zip),响应体
setHeader('Refresh', 3)三秒刷新一次
重定向setStatus(302),setHeader('location', url)或者sendRedirect(url),重定向可以重定向到任何路径,/表示自己写项目名,否则就是当前
项目,重定向会改变url地址栏的地址。
7.get和post方式有什么不同?
get请求方式将参数追加到url后面,get的url长度有限,get请求参数有限。post将请求的参数添加到请求体,请求体没有限制,post请求可以请求任意内容。
