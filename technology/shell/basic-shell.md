# shell 的基本语法

## 创建一个脚本文件`shell.sh`

```bash
$ touch shell.sh
```

在文件中输入以下内容

```shell
#!/bin/sh
echo "Hello World"
```

执行脚本文件

```bash
$ sh shell.sh
# 或者
# 赋予执行权限
$ chmod u+x shell.sh
# 执行
$ ./shell.sh
```

<span style="color: red">注：`#!/bin/sh`的作用是指定这个脚本应当以什么解释器运行，如：`#!/usr/bin/python`就是使用python解释运行这个脚本文件</span>

执行了脚本的解释器，就可以通过以下几种方式执行脚本：

**方式一：**

```bash
$ ./shell.sh
```

**方式二：**

```bash
# 先把当前目录导入path环境变量
$ export PATH=${PATH}:$(pwd)
# 执行脚本文件
$ shell.sh
# Hello World
```

**方式三：**

可以通过快捷方式执行脚本，不过是后台运行，看不到打印输出

## 变量

```shell
# 定义变量
DEMO='demo'
# 输出变量，变量的调用方式为${变量名}或$变量名
echo ${DEMO}
demo
echo $DEMO
demo
# 删除变量
unset -f DEMO
# 不会报错，只是会有空行
echo $DEMO

# 接收命令的执行结果
# 执行pwd，并保存pwd的执行结果为变量
DEMO=$(pwd)
echo ${DEMO}
/home/aszswaz/Documents/technology/shell
```

<span style="color: red">注意shell是严格区分大小写的，${DEMO} != ${demo}</span>

## 数组

### 数组的定义

数组的定义有两种方式

#### 第一种：字符串的“ ”（空格）或"\n"（换行符）

<span style="color: red">这种方式声明数组，只对指令的返回结果有效</span>

成功案例：

```shell
demo='demo01 demo02\ndemo03'
# 遍历
for arg in $(echo ${demo})
do
    echo "value: ${arg}"
done
# value: demo01
# value: demo02
# value: demo03
```

错误案例：

```shell
demo='demo01 demo02\ndemo03'
# 遍历
for arg in ${demo}
do
    echo "value: ${arg}"
done
# value: demo01 demo02
# demo03
```

应用场景：

```shell
# 卸载作为依赖安装，但是不再使用的包
sudo pacman -Rc $(pacman -Qdtq)
```

#### 第二种：(element01 element02 ....)

例：

```shell
demo=(element01 element02 element03)
for element in ${demo}
do
    echo "value: ${element}"
done
# value: element01
# value: element02
# value: element03
```

### 获得数组的元素个数

```shell
demo=(element01 element02 element03)
echo "数组元素的个数：${#demo[*]}"
# 或者
echo "数组元素的个数：${#demo[@]}"
```

### 通过下标获取数组的元素，以及遍历数组

```shell
demo=(element01 element02 element03)
echo "元素1：${demo[1]}"
echo "元素2：${demo[2]}"
echo "元素3：${demo[3]}"
```

执行结果：

```txt
元素1：element01
元素2：element02
元素3：element03
```

<span style="color: red">注意：数组的下标是从1开始，不同与其他变成语言的从0开始</span>

#### 通过while循环遍历数组

```shell
demo=(element01 element02 element03)
index=1
while ((${index} <= ${#demo[*]}))
do
    echo "第${index}个元素为：${demo[index]}"
    index=$(expr $index + 1)
done
```

结果：

```txt
第1个元素为：element01
第2个元素为：element02
第3个元素为：element03
```

#### 通过for循环进行遍历

```shell
demo=(element01 element02 element03)
for elemente in ${demo}
do
    echo "value: ${elemente}"
done
```

结果：

```txt
value: element01
value: element02
value: element03
```

## 函数

**函数的声明、使用和删除**

```shell
# 声明函数
demo(){
    echo 'Hello World'
}
# 调用函数
demo
# 删除函数
unset -f demo
```

<span style="color: red">注意：在函数内，就算命令执行出错也不会停止运行，如下所示</span>

```shell
$ demo(){
    demo01
    echo 'Hello World'
}
$ demo
demo:1: command not found: demo01
Hello World
```

**函数接收参数**

```shell
# 方式一，使用“$*”
$ demo(){
    echo "参数长度：$#"
    echo "参数列表：$*"
    for arg in $*
    do
        echo ${arg}
    done
}
# 验证结果
$ demo test01 test02
参数长度：2
参数列表：test01 test02
test01
test02
```

```shell
# 方式二。使用“$@”
$ demo(){
    echo "参数长度：$#"
    echo "参数列表：$*"
    for arg in $*
    do
        echo ${arg}
    done
}
$ demo test01 test02
参数长度：2
参数列表：test01 test02
test01
test02
```

**函数返回值**

```shell
$ demo02(){
    echo 'Hello World!'
}
$ demo(){
    # 运行函数，并且获得返回值
    echo $(demo02)
}
# 测试
$ demo
Hello Demo!
```

