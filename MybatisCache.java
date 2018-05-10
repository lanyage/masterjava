
package Register;

/**
 * Mybatis提供了一级缓存来优化数据库重复查询的问题,实现的方式就是每个SqlSession都有自己的缓存.
 * SESSION表示当前会话下的所有操作共享一个一级内存,当会话有提交的更新的时候会刷新缓存
 * STATEMENT表示缓存只对当前查询语句有效
 * 一级缓存粗度太大,而且不同的SqlSession不会共享缓存,相同的操作如果不是在同一个SqlSession下容易影起脏读
 * 不建议使用一级缓存,建议设置为STATEMENT
 */


/**
 * 此外Mybatis还设置了二级缓存,二级缓存相对一级缓存粒度更细,而且多个SqlSession可以共享一个缓存,但是设计多表查询的时候容易脏读,设计分布式的时候必定脏读
 *基于namespace的,因此mybatis的缓存设计有缺陷,不建议使用。
 * <cache/> <cache-ref/>
 *
 * 建议用Redis或者MemCache来建立缓存机制
 */
enum LocalCacheScope {
    /**
     * mybatis会话中的所有语句都会共享这个缓存,一级缓存
     */
    SESSION,
    /**
     * 缓存只对当前执行的这一个语句有效
     */
    STATEMENT;

    /**
     * 每一个SqlSession都会有自己的Executor,每一个Executor都会有自己的LocalCache,每次执行MappedStatement的时候都会查本地缓存,如果命中则返回,如果没命中就写入。
     * 1，在一个SqlSession中,多次相同的查询只会查询一次
     * 2，在一个SqlSession中,如在两个相同的查询之间执行了更新,会刷新缓存
     * 3，Session缓存只有在当前SqlSession中才能共享
     */
    private LocalCacheScope() {
    }
}
