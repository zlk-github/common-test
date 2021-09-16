### common-db-test

springboot集成mybatis-plus测试，数据库mysql5.7。包含分页、自动注入通用字段、代码生成插件等。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-db-test
    2、公共包github地址：git@github.com:zlk-github/common.git

#### 1 脚本：

--ts_权限，tt_业务，tb_基础配置


```javascript
CREATE TABLE `ts_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '名字',
    `status` TINYINT (1) DEFAULT '1' COMMENT '有效状态（0禁用，1启用）',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者id',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者id',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';
```

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

#### 4 配置类

```javascript   
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {
     /**
      * mybatis-plus分页
      * @return
      */
      @Bean
      public PaginationInterceptor paginationInterceptor(){
            return new PaginationInterceptor();
      }
}
```

#### 5 公共字段填值类

```javascript   
@Slf4j
@Component
public class CommonFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setInsertFieldValByName( "creater", getUser(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setUpdateFieldValByName("updateBy", getUser(), metaObject);
    }
}
```

#### 6 代码生成器与自定义模板

 mybatis-plus-generator代码生成器，freemarker自定义模板

```javascript   
public class CodeGeneratorUtilTest {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = "D:/code1";
        gc.setOutputDir(projectPath + "/src/main/java");
        //作者
        gc.setAuthor("zhoulikuan");
        //打开输入目录
        gc.setOpen(false);
        //xml开启BaseResultMap
        gc.setBaseResultMap(true);
        //xml开启BaseColumnList
        gc.setBaseColumnList(true);
        //gc.setServiceName("%sService");
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=GMT%2B8");
        // com.mysql.jdbc.Driver和mysql-connector-java 5一起用。
        // dsc.setDriverName("com.mysql.jdbc.Driver");
        // com.mysql.cj.jdbc.Driver和mysql-connector-java 6 一起用。比5多了一个时区
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //  pc.setModuleName(scanner("模块名"));
        pc.setModuleName(null);
        //manager
        pc.setParent("com.zlk")
                .setEntity("model.po")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller");
        mpg.setPackageInfo(pc);


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        /*// 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";*/

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //自定义模板，不需要加.ftl (.ftl为freemarker模板)
        templateConfig.setEntity("/templates/entity2.java");
        templateConfig.setMapper("/templates/mapper2.java");
        templateConfig.setService("/templates/service2.java");
        templateConfig.setServiceImpl("/templates/serviceImpl2.java");
        templateConfig.setController("/templates/controller2.java");
        templateConfig.setEntity("/templates/entity2.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //lombok模型
        strategy.setEntityLombokModel(true);
        //设置@RestController 控制器
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        // strategy.setSuperEntityColumns("id");
        //strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setInclude("ts_user");
        strategy.setControllerMappingHyphenStyle(true);
        //表前缀
        strategy.setTablePrefix("ts_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
```   
**执行器生成以下代码：7-11**

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