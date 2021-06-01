# Makefile中，一些特殊指令的作用

## .PHONY

.PHONY 后面定义的是伪目标

所谓伪目标就是这样一个目标，它不代表一个真正的文件名，在执行make时可以指定这个目标来执行其所在规则定义的命令，有时我们将一个伪目标称为标签。

为什么要使用伪目标，一种为了避免在makefile中定义的只执行命令的目标和工作目录下的实际文件出现名字冲突，另一种是提交执行makefile时的效率。

**例1：**

创建Makefile

```makefile
clean:
	touch demo.txt
	rm demo.txt
```

```bash
$ make clean
```

```txt
touch demo.txt
rm demo.txt
```

规则运行正常，在当前目录下创建一个名称为clean的文件。

```bash
$ touch clean
```

再次构建

```makefile
$ make clean
```

```txt
make: “clean”已是最新。
```

可以看到规则名和文件名出现了冲突。加上`.PHONY`

```makefile
.PHONY: clean
clean:
	touch demo.txt
	rm demo.txt
```

再次运行

```bash
$ make clean
```

```txt
touch demo.txt
rm demo.txt
```

可以看到make无视了clean文件。

