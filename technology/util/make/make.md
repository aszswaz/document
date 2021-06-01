# make

## 简介

make是一个项目构建工具，主要用于组合各种命令，成为一套构建规则，来实现构建项目的目的。

make默认会读取当前Makefile（makefile也行，首字母忽略大小写的）作为构建规则

其他的文件名称需要`make -f`指定规则文件名称。

## 简单示例

编写demo.c文件

```c
#include <stdio.h>

int main(){
    fprintf(stdout, "Hello World\n");
    return 0;
}
```

编写Makefile

```makefile
# 定义一个规则
demo:
# 编译代码
	gcc demo.c -o demo
# 运行生成的可执行文件
	demo
```

执行make

```bash
$ make demo
```

```txt
gcc demo.c -o demo
demo
Hello World
```

<span style="color: red">Makefile文件中的注释不能进行缩进，否则，也会被当成shell指令运行，虽然“#”在shell中也是注释的作用，虽然shell在接到来自make的“# \*\*\*”指令会当作注释处理，但是make会打印传递给shell的指令，如下所示</span>

```makefile
# 定义一个规则
demo:
	# 编译代码
	gcc demo.c -o demo
	# 运行生成的可执行文件
	demo
```

```bash
$ make demo
```

```txt
# 编译代码
gcc demo.c -o demo
# 运行生成的可执行文件
demo
Hello World
```

<span style="color: green">提示：如果在运行 make 时，不指定任何规则，则默认使用第一个构建规则。比如在本实例中 `$ make` = `$ make demo`</span>

