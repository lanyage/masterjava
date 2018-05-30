1.Mybatis的配置文件的结构
    <typeAliases/>mybatis的别名,底部使用的一个hashMap来register别名
    <mappers>
        <mapper resource/name/type>
    </mappers>映射文件
    <environments>
        <environment id="development">
            <transactionManager></transactionManager>
            <dataSource>
                <property name type/>
            </dataSource>
        </environment>
    </environments>事务管理和数据库连接池
2.mybatis简介
    mybatis是一款优秀的orm框架,避免了JDBC硬编码,底层封装了ognl表达式,支持动态sql语句,开发者仅仅需要通过xml或者注解就能非常好的管理sql
    语句,不需要花时间在参数注入和结果集的处理,并且能够通过#占位符来防止sql注入。
    但是mybatis的映射文件针对不同的数据库,需要维护不同版本的mapper文件。
    mybatis推荐使用mapperProxy的动态代理来实现数据库的操作,其实就是面向接口编程
    SqlSessionFactoryBuilder->SqlSessionFactory->SqlSession->Mapper->Executor->调用底层的jdbc api
3.mybatis的parameterType
    Vo,如parameterType="PersonVo",javaVo类用来作为查询条件，javaPo类来封装查询结果
    list,如parameterType="list",<forEach collection="list" ... >
    array,parameterType = "array",<forEach collection="array">
    map,parameterType="java.util.HashMap"
    普通类型
4.mybatis的resultMap和resultType
    resultType为普通的java类型,resultMap则用于封装复杂的类型
    <resultMap id="personResult" type="Person">
            <id property="id" column="id"></id>
            <result property="name" column="name"></result>
            <association property=""></association>
            <collection property=""/>
    </resultMap>
5.mybatis动态查询
    <if>
    <where>
6.延迟加载
    需要查询关联信息时,懒加载可以有效减少数据库压力,也就是首次查询只查询主要信息,关联信息等用户获取时再加载
    如果不使用mybatis的延迟加载,我们可以用两个mapper文件来实现延迟加载
7.Mybatis缓存
    mybatis的缓存分为1，2级缓存
    1级缓存作用域是SqlSession,2级缓存是多个SqlSession共享的
    1级缓存底层使用的是HashMap来维护的,key为hashcode+sqlId+sql,所有的delete,update,insert操作的commit都会清空缓存
    2级缓存是基于namespace划分的,<setting name="cacheEnabled" value="true"/>来开启二级缓存,二级缓存需要缓存对象实现序列化
    如果想关闭二级缓存就在statement中useCache="false",使用flushCache="true"来刷新缓存
7.Mybatis整合ehcache
    ehcache是一个纯java的进程内缓存框架,是一种广泛使用的开源java分布式缓存,具有快速、精干的特点,是hibernate默认的CacheProvider
    一 引入依赖
    <dependency>
               <groupId>org.mybatis.caches</groupId>
               <artifactId>mybatis-ehcache</artifactId>
               <version>1.0.2</version>
    </dependency>
    二 在classpath下创建ehcache.xml
    <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
        <diskStore path="F:\develop\ehcache"/>
        <defaultCache
           maxElementsInMemory="1000"
           maxElementsOnDisk="10000000"
           eternal="false"
           overflowToDisk="false"
           timeToIdleSeconds="120"
           timeToLiveSeconds="120"
           diskExpiryThreadIntervalSeconds="120"
           memoryStoreEvictionPolicy="LRU">
        </defaultCache>
    </ehcache>
    三 开启ehcache缓存
    修改mapper.xml,<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
    按需调整缓存参数
    <cache type="org.mybatis.caches.ehcache.EhcacheCache">
            <property name="timeToIdleSeconds" value="3600"/>
            <property name="timeToLiveSeconds" value="3600"/>
            <property name="maxEntriesLocalHeap"value="1000"/>
            <property name="maxEntriesLocalDisk" value="10000000"/>
            <property name="memoryStoreEvictionPolicy" value="LRU"/>
    </cache>
8.Mybatis二级缓存应用场景
    对于访问多的查询请求且用户对查询结果实时性要求不高,此时可以用Mybatis二级缓存来降低数据库的访问量,比如耗时的sql查询和电话账单查询sql等
    局限性:二级缓存粒度比较粗,对于实时性比较高的查询比如给用户显示最新商品信息,如果使用二级缓存就无法做到在改变一个商品信息的时候不用刷新其
    它商品信息,因为二级缓存基于namespace,只要在该namespace有delete,update或者insert操作都会清除缓存,因此对于这种场景,我们还是需要外部
    的缓存技术,如果redis,memcache等技术。
9.整合Spring
    当Mybatis整合spring的时候,数据库连接池,sqlsessionfactory都可以交给spring去管理了。
    <context:property-placeholder location=""/>
    <bean id="dataSource" class="" destroy-method="close"></bean>
    <bean id="sqlSessionFactory" class="SqlSessionFactoryBean">
        dataSource
        configLocation
    </bean>
