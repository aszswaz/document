# logback

### 添加maven依赖

logback-classic 模块需要在 classpath 添加 slf4j-api.jar、logback-core.jar 以及 logback-classic.jar。

### 配置 logback

单独使用logback的时候，配置文件查找顺序如下

1. logback 会在类路径下寻找名为 logback-test.xml 的文件。
2. 如果没有找到，logback 会继续寻找名为 logback.groovy 的文件。
3. 如果没有找到，logback 会继续寻找名为 logback.xml 的文件。
4. 如果没有找到，将会通过 JDK 提供的 ServiceLoader 工具在类路径下寻找文件 META-INFO/services/ch.qos.logback.classic.spi.Configurator，该文件的内容为实现了 Configurator 接口的实现类的全限定类名。
5. 如果以上都没有成功，logback 会通过 BasicConfigurator 为自己进行配置，并且日志将会全部在控制台打印出来。

如果你使用的是 maven，你可以在 src/test/resources 下新建 logback-test.xml。maven 会确保它不会被生成。所以你可以在测试环境中给配置文件命名为 logback-test.xml，在生产环境中命名为 logback.xml。

**通过系统属性指定默认的配置文件**

通过系统属性 logback.configurationFile 可以指定默认的配置文件的路径。它的值可以是 URL，类路径下的文件或者是应用外部的文件。

```shell
java -Dlogback.configurationFile=/path/to/config.xml chapters.configuration.MyApp1
```

注意：文件类型只能是 ".xml" 或者 ".groovy"，其它的拓展文件将会被忽略。

因为 logback.configureFile 是一个系统属性，所以也可以在应用内进行设置。但是必须在 logger 实例创建前进行设置。

**当配置文件更改时，自动加载**

为了让 logback 能够在配置文件改变的时候自动去扫描，需要在 <configuration> 标签上添加 scan=true 属性。

```xml
<configuration scan="true">
    ...
</configuration>
```

默认情况下，一分钟扫描一次配置文件，看是否有更改。通过 <configuration> 标签上的 scanPeriod 属性可以指定扫描周期。扫描周期的时间单位可以是毫秒、秒、分钟或者小时。

```xml
<configuration scan="true" scanPeriod="30 seconds">
   ...
</configuration>
```

注意：如果没有指定时间单位，则默认为毫秒。

当设置了 scan="true"，会新建一个 ReconfigureOnChangeTask 任务用于监视配置文件是否变化。ReconfigureOnChangeTask 也会自动监视外部文件的变化。如果更改后的配置文件有语法错误，则会回退到之前的配置文件。

**直接调用 JoranConfigurator**

Logback 依赖的配置文件库为 Joran，是 logback-core 的一部分。logback 的默认配置机制为：通过 JoranConfigurator 在类路径上寻找默认的配置文件。你可以通过直接调用 JoranConfigurator 的方式来重写 logback 的默认配置机制。