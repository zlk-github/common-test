### common-db-test

springboot集成redis测试，数据库mysql5.7。包含redis做缓存(默认过期时间),分布式锁，布隆表达式，消息传递/发布订阅, Redis 事务。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git

#### 2 pom.xml(完整)

```javascript   

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
<parent>
<artifactId>common-test</artifactId>
<groupId>org.zlk</groupId>
<version>1.0-SNAPSHOT</version>
</parent>
<modelVersion>4.0.0</modelVersion>

    <artifactId>common-db-test</artifactId>

    <properties>
        <java.version>1.8</java.version>
        <commons-lang3.version>3.7</commons-lang3.version>
        <lombok.version>1.18.6</lombok.version>
        <mybatis-plus.version>3.1.0</mybatis-plus.version>
        <mybatis-plus-generator.version>3.2.0</mybatis-plus-generator.version>
        <freemarker.version>2.3.29</freemarker.version>
        <springfox-swagger2.version>2.9.2</springfox-swagger2.version>
        <alibaba.druid.version>1.1.21</alibaba.druid.version>
        <!-- <mysql-connector.version>5.1.38</mysql-connector.version>-->
        <mysql-connector.version>8.0.11</mysql-connector.version>
        <freemarker.version>2.3.29</freemarker.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- 引入Druid依赖，阿里巴巴所提供的数据源 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>${alibaba.druid.version}</version>
        </dependency>

        <!-- 提供mysql驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql-connector.version}</version>
        </dependency>


        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!--mybatis-plus代码生成器start-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>${mybatis-plus-generator.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.freemarker/freemarker -->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>${freemarker.version}</version>
        </dependency>
        <!--mybatis-plus代码生成器end-->

        <!-- springfox-swagger2 start -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${springfox-swagger2.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${springfox-swagger2.version}</version>
        </dependency>
        <!-- springfox-swagger2 end -->

       <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${commons-lang3.version}</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
    
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 3 application.yaml

说明：

    com.mysql.jdbc.Driver和mysql-connector-java 5一起用。
    com.mysql.cj.jdbc.Driver和mysql-connector-java 6 一起用。比5多了一个时区。

application.yaml如下

```javascript   
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=GMT%2B8
        username: root
        password: 123456
        driver-class-name: com.mysql.jdbc.Driver

# 配置slq打印日志
mybatis-plus:
    #原生配置
    configuration:
        #开启驼峰功能
        map-underscore-to-camel-case: true
        cache-enabled: false
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  #关键部分。用来显示sql
```

#### 7 model.po实体层

```javascript   
/**
* @author  likuan.zhou
* @title:  User对象
* @description: User对象 (注释部分为未调试，需要添加配置文件，见https://my.oschina.net/654476371/blog/3054503)
* @date 2021-09-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_user")
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @ApiModelProperty(value = "有效状态（0禁用，1启用）")
    private Integer status;

    @ApiModelProperty(value = "创建者id")
    //@TableField( fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //@TableField( fill = FieldFill.INSERT)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者id")
    //@TableField( fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@TableField( fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

#### 8 mapper 层

```javascript   
/**
 * @author  likuan.zhou
 * @title:  UserMapper
 * @description: 用户mapper
 * @date 2021-09-14
 */
 //@Mapper
 public interface UserMapper extends BaseMapper<User> {

 }
```

#### 9 service 层

```javascript   
/**
 * @author  likuan.zhou
 * @title:  IUserService
 * @description: 用户业务接口
 * @date 2021-09-14
 */
public interface IUserService extends IService<User> {
}
```

#### 10 serviceImpl 层

```javascript   
/**
 * @author  likuan.zhou
 * @title:  UserServiceImpl
 * @description: 用户业务接口实现
 * @date 2021-09-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
}
```

#### 11 controller 层 （省略）

#### 12 测试用例

```javascript   
/**
* @author likuan.zhou
* @title: IUserServiceTest
* @projectName common-test
* @description: 用户业务测试
* @date 2021/9/15/015 19:37
*/
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class IUserServiceTest {
  @Autowired
  private IUserService userService;

  @Test
  public void testPageList1() {
      UserParam userParam = new UserParam();
      userParam.setPageNo(1L);
      userParam.setPageSize(10);
      PageBean<UserVO> pageList = userService.pageList(userParam);
      System.out.println(pageList.getTotal());
      assertNotNull(pageList);
  }

  @Test
  public void testPageList() {
      LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
      userLambdaQueryWrapper.like(User::getName , "name");
      userLambdaQueryWrapper.eq(User::getStatus,1);
      Page<User> userPage = new Page<>(1 , 10);
      IPage<User> userIPage = userService.page(userPage, userLambdaQueryWrapper);
      System.out.println(userIPage);
      assertNotNull(userIPage);
  }

  @Test
  public void testQueryList() {
      LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
      userLambdaQueryWrapper.like(User::getName , "name");
      userLambdaQueryWrapper.eq(User::getStatus,1);
      //查询列表
      List<User> list = userService.list(userLambdaQueryWrapper);
      System.out.println(list);
      assertNotNull(list);
  }

  @Test
  public void testGetById() {
      //id查询
      User user = userService.getById(1L);
      System.out.println(user);
      assertNotNull(user);
  }

  @Test
  public void testSave() {
      User user = new User();
      user.setName("名字");
      user.setStatus(1);
      //新增
      userService.save(user);

      //(需要返回主键id需要自定义sql) 需要自己封装SQL
      /*<insert id="insertUser" parameterType="cn.bear.neckmassager.api.entity.Userinfo" useGeneratedKeys="true" keyProperty="id">
      insert into userinfo(user_name,create_time) values (#{userName},#{createTime});
      </insert>*/

       //更新活保存 userService.saveOrUpdate(user)
       //批量新增  userService.saveBatch(userList);
  }

  @Test
  public void testUpdateById() {
      User user = new User();
      user.setId(1L);
      user.setName("名字");
      user.setStatus(1);
      //更新
      userService.updateById(user);
  }

  @Test
  public void testRemoveById() {
      //id删除
      userService.removeById(1L);
  }
}
```

### 待补充：

字段自动填充、日志输出管理、sql拦截注入、封装新增返回主键id方法、sql多表查询、切面异步操作日志等。

### 参考

    mybatis-plus官网: https://mp.baomidou.com/

    mybatis-plus源码地址：https://github.com/baomidou/mybatis-plus/
    
    mybatis-plus代码生成器：https://github.com/15706058532/MyBatisPlusGenerator

    AOP切面：https://blog.csdn.net/fz13768884254/article/details/83538709