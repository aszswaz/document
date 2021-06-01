# maven指令介绍

```bash
$ mvn clean # 清理项目, 主要是删除项目的target文件夹, 如果里面的文件被其他程序使用, 会导致删除失败
$ mvn package # 打包指令, 将项目打成jar包, 但是默认是打成一个不可运行的jar包, 打成一个可运行的jar包需要在pom.xml中引入编译插件
$ mvn package -Dmaven.test.skip=true # 打包时跳过单元测试
$ mvn package -P profile # 打包时使用定制的项目环境配置
$ mvn clean package -Dmaven.test.skip=true -P test # 组合指令, 功能不变
$ mvn compile # 编译项目
$ mvn compile # 编译项目并使用定制的项目配置
$ mvn clean compile -P test # 组合指令, 同上. 不过默认不会进行单元测试, 所以不需要配置-Dmaven.test.skip=true
$ mvn exec:java -Dexec.mainClass="com.vineetmanohar.module.Main" -P test # 通过maven来启动java程序, maven会自动将项目中依赖的jar包添加到classpath, 不过在执行该指令前需要先执行compile编译代码, 也不能缺少指定的环境, 编译时用的什么环境, 运行时也得什么环境
# 安装本地的jar包到maven仓库, 然后在项目的pom.xml文件中引入即可
$ mvn install:install-file -Dfile=D:/taobao-sdk-java-auto-20160607.jar -DgroupId=com.ganshane.specs -DartifactId=taobao-sdk-java-auto-20160607 -Dversion=1.0.0 -Dpackaging=jar
```

