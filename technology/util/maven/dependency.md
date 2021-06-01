### maven dependency依赖树分析

使用 `mvn dependency:tree` 即可展示全部的

可以加上Dincludes或者Dexcludes进行筛选格式 groupId:artifactId:version的方式进行过滤
例如

```bash
$ mvn dependency:tree -Dverbose -Dincludes=com.google.guava:guava
```

maven打印依赖树到文件中：

```shell
$ mvn dependency:tree >> D:/tree.txt
```

### 寻找依赖树中的groupId

```shell
$ mvn dependency:tree -Dverbose -Dincludes=org.slf4j
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< com.zhiwei:data-manage >-----------------------
[INFO] Building data-manage 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-dependency-plugin:3.1.2:tree (default-cli) @ data-manage ---
[INFO] Verbose not supported since maven-dependency-plugin 3.0
[INFO] com.zhiwei:data-manage:jar:0.0.1-SNAPSHOT
[INFO] +- org.springframework.boot:spring-boot-starter-data-mongodb:jar:2.4.1:compile
[INFO] |  \- org.springframework.data:spring-data-mongodb:jar:3.1.2:compile
[INFO] |     \- org.slf4j:slf4j-api:jar:1.7.30:compile
[INFO] \- org.springframework.boot:spring-boot-starter-log4j2:jar:2.4.1:compile
[INFO]    \- org.slf4j:jul-to-slf4j:jar:1.7.30:compile
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.841 s
[INFO] Finished at: 2021-01-12T13:03:12+08:00
[INFO] ------------------------------------------------------------------------
```

