### common-db-test

数据库mybatis-plus集成测试，包含分页，自动注入通用字段，代码生成插件等。

源码地址见：

#### 1 脚本：

--ts_权限，tt_业务，tb_基础配置

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

#### 2 pom.xml(完整)
    
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
                <artifactId>druid</artifactId>
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

#### 3 application.yaml

#### 4 配置类

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

#### 5 公共字段填值类

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

#### 6 代码生成器与自定义模板

 mybatis-plus-generator代码生成器，freemarker自定义模板

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
            // mysql 5.7
            // dsc.setDriverName("com.mysql.jdbc.Driver");
            // mysql 8.0
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
 
**执行器生成以下代码：**

#### 7 model.po实体层

#### 8 mapper 层

#### 9 service 层

#### 10 serviceImpl 层

#### 11 controller 层

#### 12 测试用例

### 参考

    mybatis-plus官网: https://mp.baomidou.com/

    mybatis-plus源码地址：https://github.com/baomidou/mybatis-plus/
    
    mybatis-plus代码生成器：https://github.com/15706058532/MyBatisPlusGenerator
