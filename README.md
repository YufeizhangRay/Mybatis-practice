# mybatis-practice
Mybatis框架学习实践  
  
什么是`MyBatsis`？  

>`MyBatsis`是支持自定义`SQL`、存储过程和高级映射的一流持久化框架，它几乎消除了所有的`JDBC`代码，可以使用简单的XML或注释来配置`Java POJOS`（朴素的`Java`对象）到数据库记录的映射关系。  
  
`MyBatis` 配置文件：  
>`mybatis-config.xml`： 此文件作为 `MyBatis` 的全局配置文件，配置了 `MyBatis` 的运行环境等信息。  
`xxxMapper.xml`： 即 `SQL` 映射文件，文件中配置了操作数据库的 `SQL` 语句。此文件需要在 `mybatis-config.xml` 中加载。  
  
与`Hibernate`的对比：  
>`Hibernate`：`Hibernate`是当前最流行的`ORM`框架之一，对`JDBC`提供了较为完整的封装。`Hibernate`的`O/R Mapping`实现了`POJO` 和数据库表之间的映射，以及`SQL`的自动生成和执行。  
`Mybatis`：`Mybatis`同样也是非常流行的`ORM`框架，主要着力点在于 `POJO `与` SQL `之间的映射关系。然后通过映射配置文件，将`SQL`所需的参数，以及返回的结果字段映射到指定` POJO `。相对`Hibernate“O/R”`而言，`Mybatis` 是一种`“Sql Mapping”`的`ORM`实现。  
  
>`Mybatis`：小巧、方便、高效、简单、直接、半自动化   
`Hibernate`：强大、方便、高效、复杂、间接、全自动化  
  
