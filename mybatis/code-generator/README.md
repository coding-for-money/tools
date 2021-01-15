
代码自动生成工具

一、简介

>概述

CodeGenerator是一个轻量的代码生成工具，可以在常规的项目开发初始阶段生成model、dao、Mapper、
mapper（mybatis）、service、controller，项目思路来源于mybatis－generator，不过代码更加简洁易控制

 - 修复了关键字字段时SQL报错问题，关键字自动加上解析符号
 
 - 优化数据库字段如果是tinyint是属性变成Integer属性
 
 - 优化数据库字段如果是text是属性变成String属性

>特性

  - 1、代码运行既可以下载源码运行AppTest测试用例，也可以使用maven插件的方式运行（推荐使用）
  - 2、代码模块可以自定义生成，比如只需要model、dao，Mapper、mapper的代码，可以在配置文件配置，生成目录也可以配置，规则是约定大于配置
  - 3、可以修改自定义的模版样式

>使用方式

  - 1、首页check源码在本地，如果需要本地测试用例运行，请看AppTest

```java
 
  public class AppTest {

    private static ApplicationContext context;

    private static GeneratorFactoryImpl generatorFactory;

    @BeforeClass
    public static void beforeClass(){
        try {
            context = new ClassPathXmlApplicationContext("classpath:spring-generator.xml");
            generatorFactory=(GeneratorFactoryImpl)context.getBean("generatorFactory");
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void doAfter(){
        if(context !=null && context instanceof ClassPathXmlApplicationContext){
            ((ClassPathXmlApplicationContext) context).close();
        }
    }

    @Test
    public void codeGeneratorTest() {
        generatorFactory.defaultGeneratorStarter();
    }
}

```

  - 2、运行之前需要修改一下spring-generator.xml，配置每个模块的生成器

```xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    <bean id="modelPackageConfigTypes" class="com.llj.mybatis.config.PackageConfigTypes">
        <constructor-arg name="type" value="MODEL"/>
        <constructor-arg name="packageConfigTypeSet">
            <set>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="poPackage"/>
                    <property name="targetDir" value="/po"/>
                    <property name="fileNameSuffix" value="PO.java"/>
                    <property name="template" value="domain_po.vm"/>
                </bean>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="modelPackage"/>
                    <property name="targetDir" value="/model"/>
                    <property name="fileNameSuffix" value="Model.java"/>
                    <property name="template" value="domain_model.vm"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>

    <bean id="mapperPackageConfigTypes" class="com.llj.mybatis.config.PackageConfigTypes">
        <constructor-arg name="type" value="MAPPER"/>
        <constructor-arg name="packageConfigTypeSet">
            <set>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="targetDir" value="/dao/mapper"/>
                    <property name="fileNameSuffix" value="Mapper.xml"/>
                    <property name="template" value="dao_sqlMapper.vm"/>
                </bean>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="mapperPackage"/>
                    <property name="targetDir" value="/dao"/>
                    <property name="fileNameSuffix" value="DAO.java"/>
                    <property name="template" value="dao_mapper.vm"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>

    <bean id="mapperConfigPackageConfigTypes" class="com.llj.mybatis.config.PackageConfigTypes">
        <constructor-arg name="type" value="MAPPER_CONFIG"/>
        <constructor-arg name="packageConfigTypeSet">
            <set>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="targetDir" value="/dao/mapper"/>
                    <property name="fileNameSuffix" value="mybatis-config.xml"/>
                    <property name="template" value="mybatis-config.vm"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>

    <bean id="resultPackageConfigTypes" class="com.llj.mybatis.config.PackageConfigTypes">
        <constructor-arg name="type" value="RESULT"/>
        <constructor-arg name="packageConfigTypeSet">
            <set>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="resultPackage"/>
                    <property name="targetDir" value="/service/module"/>
                    <property name="fileNameSuffix" value="Result.java"/>
                    <property name="template" value="result.vm"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>

    <bean id="servicePackageConfigTypes" class="com.llj.mybatis.config.PackageConfigTypes">
        <constructor-arg name="type" value="SERVICE"/>
        <constructor-arg name="packageConfigTypeSet">
            <set>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="servicePackage"/>
                    <property name="targetDir" value="/service"/>
                    <property name="fileNameSuffix" value="Service.java"/>
                    <property name="template" value="service.vm"/>
                </bean>
                <bean class="com.llj.mybatis.config.PackageConfigType">
                    <property name="aliasType" value="serviceImplPackage"/>
                    <property name="targetDir" value="/service/impl"/>
                    <property name="fileNameSuffix" value="ServiceImpl.java"/>
                    <property name="template" value="service_impl.vm"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>

    <bean id="modelGenerator" class="com.llj.mybatis.generator.impl.ModelGeneratorImpl">
        <property name="packageConfigTypes" ref="modelPackageConfigTypes"/>
    </bean>
    <bean id="mapperGenerator" class="com.llj.mybatis.generator.impl.MapperGeneratorImpl">
        <property name="packageConfigTypes" ref="mapperPackageConfigTypes"/>
    </bean>
    <bean id="mapperConfigGenerator" class="com.llj.mybatis.generator.impl.MapperConfigGeneratorImpl">
        <property name="packageConfigTypes" ref="mapperConfigPackageConfigTypes"/>
    </bean>
    <bean id="resultGenerator" class="com.llj.mybatis.generator.impl.ResultGeneratorImpl">
        <property name="packageConfigTypes" ref="resultPackageConfigTypes"/>
    </bean>
    <bean id="serviceGenerator" class="com.llj.mybatis.generator.impl.ServiceGeneratorImpl">
        <property name="packageConfigTypes" ref="servicePackageConfigTypes"/>
    </bean>
    <bean id="generatorFactory" class="com.llj.mybatis.generator.base.GeneratorFactoryImpl">
        <property name="generatorSet">
            <set>
                <bean parent="modelGenerator"/>
                <bean parent="mapperGenerator"/>
                <bean parent="mapperConfigGenerator"/>
                <bean parent="resultGenerator"/>
                <bean parent="serviceGenerator"/>
            </set>
        </property>
    </bean>
</beans>

```

  - 3、运行之前需要修改一下config-generator.properties，配置数据源等信息

```properties

##mysql连接配置
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://127.0.0.1:3306/shiro
jdbc.username=root
jdbc.password=root

generator.authorName=llj

##是否生成注释
generator.annotation=false

##生成代码位置
generator.location=src

##文件包名称
generator.project.name=main

##生成那些层
generator.layers=mapper,mapperConfig,model,service,result

##包名称
generator.basePackage=com.llj.xxx

##表名称，多个用逗号分隔(,)
generator.tables=sys_privilege_menu

##过滤掉代码表的前缀
generator.table.prefix=sys_

#去除代码表的后缀
generator.table.suffix=

#去除代码表字段的前缀
generator.column.prefix=F

##浮点型转化为：BigDecimal，否则转化为：Double
generator.precision=high

```

 - 4、maven插件使用方式

在pom文件中定义插件

```xml
<plugin>
    <groupId>com.oneplus.maven.plugins</groupId>
    <artifactId>code-generator</artifactId>
    <version>${codeGenerator.version}</version>
</plugin>
```

 - 5、代码生成略览