# Makefile基本语法

<span style="color: green">Makefile包含五种内容：显式规则， 隐式规则，变量定义，指令和注释。</span>

## 显式规则

**例1**

```makefile
# 定义一个规则
demo: demo.c
# 编译代码
	gcc demo.c -o demo
# 运行生成的可执行文件
	demo
```

以例1为例：

`demo: demo.c`：

​	“demo”是一个规则名称，使用make时需要指定规则的名称才能运行相应的规则，比如`$ make demo`
​	“demo.c”是文件名称，表示运行规则需要哪些文件，如果缺少这些必要的文件，将不会运行这个构建规则，同时输出相应的错误提示。比如，把例1中的`demo: demo.c`改为`demo: demo.c demo-02.c`，运行`$ make demo`，输出的结果为：`make: *** 没有规则可制作目标“demo-02.c”，由“demo” 需求。 停止。`

## 变量

<span style="color: green">在Makefile中可以使用变量，来使一个值在多处使用</span>

```makefile

# 所有的代码文件
obj = demo.c demo_01.h demo_01.c

# 定义一个规则，检查变量中的文件是否存在
demo: ${obj}
# 编译代码
	gcc ${obj} -o demo
# 运行生成的可执行文件
	demo
```

`obj = demo.c demo_01.h demo_01.c`定义变量，`${obj}`或`$(obj)`引用变量

<span style="background-color: greenyellow">每makefile文件有一个变量命名 objects，OBJECTS，objs，OBJS，obj，或者OBJ这是所有目标文件名的列表，这是标准的做法。</span>

## 隐式规则

**Makefile**

```makefile
demo: demo.o
	cc main.c -o main demo.o
```

`demo: demo.o`中，由于当前的目录下并不存在“demo.o”文件，make会在当前目录中寻找“demo.*”文件，并且根据不同的文件后缀，使用不同的编译器生成“demo.o”文件。比如“demo.c”使用“cc”（在linux系统下cc是gcc的一个硬连接）编译，“demo.p”就使用Pascal编译器。

`cc main.c -o main demo.o`的中的“demo.o”就只是一个已经编译好的文件了，cc编译器输出的“.o”文件就是汇编的二进制文件，只是还没有进行可执行程序打包。

以上面的Makefile为例：

创建`demo.h`文件

```c
int demo();
```

创建`demo.c`文件

```c
#include "stdio.h"

int demo() {
    fprintf(stdout, "Hello World\n");
    return 0;
}
```

构建：

```bash
$ make demo
```

```txt
cc    -c -o demo.o demo.c
cc main.c -o main demo.o
```

运行：

```bash
$ main
```

```txt
Hello World
```

可以看到在构建时，先生成demo.o文件，之后再生成可执行文件。

**隐式规则也是可以使用显式规则的所有语法，但是最终生成的文件名称最好（非必须）与规则名称相同，这样可以避免出现混淆。**

如下几种Makefile当中也是隐式规则的用法：

只写明需要的文件，不写具体的编译指令：

```makefile
demo: demo.o
	cc main.c -o main demo.o

demo.o: demo.h demo.c
```

写明规则的全部内容：

```makefile
demo: demo.o include/demo.h
	cc main.c -o main demo.o -Iinclude

demo.o: demo.c
	@echo "正在运行规则："$@
	@echo "开始编译文件："$^
	cc -o $@ -c $^
```

| 特殊符号 | 说明                           |
| -------- | ------------------------------ |
| @echo    | 打印信息                       |
| $@       | 当前规则名称                   |
| $^       | 当前规则请求的必须要存在的文件 |

**makefile的其他样式**

```makefile
objects = main.o kbd.o command.o display.o \
          insert.o search.o files.o utils.o

edit : $(objects)
        cc -o edit $(objects)

# 给objects变量中的所有隐式规则，指定先决条件
$(objects) : defs.h
# 给多个隐式规则指定先绝体条件
kbd.o command.o files.o : command.h
display.o insert.o search.o files.o : buffer.h
```
