## 说明
本工程主要用来如下用途
 * 所有通用代码，通用业务逻辑，能用对象等
 * 系统通用工具类，一般用hutool，如果hutool中没有，则自行编写同名称类，继承hutool
 * 数据库实体映射
 * 数据库配置
 
由于工具类中引用了mybatis，如子系统不需要访问数据库，则要在Spring启动类中加上如下代码
    
    @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

## 工具类
https://hutool.cn/docs