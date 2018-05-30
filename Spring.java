1.什么是Spring?
    Spring是一个分层的架构,由7个定义良好的模块组成,Spring模块构建在核心容器之上,核心容器定义了创建、配置和管理Bean的方式
    核心容器:Spring-core
    Spring上下文:Spring-context
    Spring Aop:面向切面特性
    Spring Dao:构建数据库异常的层次结构
    Spring ORM:插入了Hibernate,ibatis SQl Map
    Spring Web:web上下文构建在Spring上下文之上
    Spring MVC:Spring mvc是一个全功能的构建web应用程序的MVC实现。
    Spring核心是:支持不绑定到特定J2EE服务的可崇勇业务和数据访问对象。
2.IOC和AOP
    Spring IOC采用setter或者构造器来注入。
    Spring AOP的核心是切面,是将能够影响多个类的行为封装到可重用的模块中去。
    比如说日志:传统的日志的记录需要将日志记录语句放在所有的方法中,在AOP的方式中,可以通过把这种行为抽离出来模块化,然后通过声明式将其应用到
    需要日志的组件上,JAVA类不需要知道日志服务的存在,也不需要考虑相关的代码,因此SpringAop编写的应用程序代码是松散耦合的,AOP在Spring的应
    用主要是日志和事物管理。
3.IOC
    IOC API最高抽象是BeanFactory,它是工厂设计模式的实现,允许通过名称检索和创建对象,BeanFactory也可以管理对象之间的关系
    BeanFactory支持两类对象模型:Singleton和Prototype,前者是默认的也是最常用的对象模型,对于无状态服务的对象很理想。后者确保每次检索都会
    创建新的对象,当用户每次都需要自己的对象,原型模式最适合。
    BeanFactory最常用的实现是XmlBeanFactory,BeanFactory factory = new XMLBeanFactory(new FileInputSteam("bean.xml"));然后调
    用MyBean bean = (MyBean) factory.getBean("beanName");
4.AOP
    AOP是作为面向对象编程的一种补充,Aspect Orient Programing
    存在价值:OOP面向对象编程在软件规模的增大,应用的逐渐升级,慢慢出现了很多OOP不能解决的问题。
    编译器增强:AspectJ
    运行时增强:动态代理
    Spring默认使用动态代理来实现AOP,如果想对象AspectJ配置支持,需要在配置文件中增加<aop:aspectj-autoproxy/>,若需要支持注解的AspectJ
    @AspectJ支持,<bean class="org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator"/>
    总结:无论是AspectJ的静态AOP代理还是Spring AOP的动态Aop代理,本质上都是创建Aop代理来增强某种行为。
5.Spring事物
    Spring提供了两种管理事物的方式,编码式和声明式
    传统的JDBC事务管理
    Connection conn = DriverManager.getConnection();
    try {
        conn.setAutoCommit(false);
        执行CRUD操作
        conn.commit();
    } catch (Exception e) {
        conn.rollback();
        e.printStackTrace();
    } finally {
        conn.close();
    }
    什么是事物?事物是一系列数据库操作构成的单元,这些操作要么全部执行成功,要么全部失败
    事物的四大特性:原子性,一致性,隔离性(保证并发事物执行之间无影响,这个需要设置事物隔离级别),持久性
    事物并发产生的问题:脏读,不可重复度,幻读,丢失更新(撤销一个事物时,把其他事物执行的更新覆盖了)
    JDBC5中事物隔离级别:
        NONE(不支持事物)
        READ_UNCOMMITED(允许脏读,不可重复读,幻读)
        READ_COMMITED(不允许脏读,但是允许不可重复读和幻读) MOST_POPULAR,不能达到的用数据库的锁来实现
        REPEATABLE_READ(不允许脏读和不可重复度,但允许幻读)
        SERIALIZABLE(不允许脏读,不可重复度和幻读)
    可以通过conn.setTransactionIsolation(Connection.TRANSACTION_NONE)来设置隔离级别

    Spring事物到底有什么创新？
    有了Spring的生命式事物,再也不需手动的创建连接,关闭连接,事物提交,事物回滚等操作了,使得我们可以更加专注于业务,Spring不是直接管理事物,
    而是提供了多种事物管理器,他们将事物管理委托给Hibernate,JPA,Mybatis等持久化机制所提供的相关平台下的事物来实现.
    Spring的事物管理的核心接口是PlatformTransactionManager,事物管理器通过getTransaction(TransactionDefinition definition)方法
    根据事物的传播行为来返回当前对象或创建一个新事物。

    TransactionDefinition的API:
        int getPropagationBehavior();获取事物的传播行为
        int getIsolationLevel();获取事物的隔离界别
        int getTimeout();获取事物执行失败的间隔时间
        boolean isReadOnly();事物是否只读
        String getName();返回事物的名称
    事物的7种传播行为(也就是当事物被一个事物调用时,这个事物应当如何进行):
        int PROPAGATION_REQUIRED = 0; 支持当前事物,如果当前没有事物,就创建一个事物,是Spring默认的事物传播行为
        int PROPAGATION_SUPPORTS = 1; 支持当前事物,如果当前没有事物,就以非事物的方式执行
        int PROPAGATION_MANDATORY = 2;支持当前事物,如果当前没有事物,就抛出异常
        int PROPAGATION_REQUIRES_NEW = 3;新建事物,如果当前事物存在,就挂起当前事物
        int PROPAGATION_NOT_SUPPORTED = 4;以非事物的方式执行,如果当前事物存在,就挂起当前事物
        int PROPAGATION_NEVER = 5;以非事物的方式执行,如果当前事物存在,就抛出异常
        int PROPAGATION_NESTED = 6;如果当前事物存在,则嵌套执行,否则和PROPAGATION_REQUIRED类似,外层事务的回滚可以引起内层事务的回滚
        例子:
            @Transactional(propagation = Propagation.REQUIRED)
            public void methodA() {
             methodB();
            // do something
            }

            @Transactional(propagation = Propagation.REQUIRED)
            public void methodB() {
                // do something
            }
            解析:单独调用事物B的时候,因为当前上下文不存在事物,所以会创建一个新的事物,单独调用事物A的时候,当前事物不存在,那么会开启一个事
            物,当执行到B的时候,因为当前事物存在了,那么B就会加入A
