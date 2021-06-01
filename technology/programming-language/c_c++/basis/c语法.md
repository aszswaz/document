# c代码结构

## 预处理指令（prerocessor directives）

```c
#include <stdic.h>
#define MAX_COLS 20
```

这两行称为预处理指令，他们是由**预处理器**解释的。预处理器读入源代码，根据源代码指令对其进行修改，然后把修改过的源代码交给编译器。

## 函数原型(function prototype)

```c
int demo(int arrays[], int max);
```

函数原型告诉编译器这些以后将在源文件中定义的函数的特征。这样，当这些函数被调用时，编译器就能对他们进行准确性检查。

## 指针(pointer)

指针指定一个存储于计算机内存中的值的地址。

<span style="color: red">指针变量的占用内存大小，取决于硬件（或者操作系统）的寻址能力，16位的操作系统中指针是2个字节，32位是4个字节，64位是8个字节</span>

## 过程（procedure）

无返回值的函数被称为过程

## 使用多个文件编写代码

多文件写代码主要使用两种形式:

使用头文件声明函数原型, 再以另外的文件编写函数真正的实现

```c
#include <stdio.h>
#include "demo.h"
#include "config.c"

static int b;

int main() {
    demo();
    printf("main: %d\n", b);
    printf("main: %d\n", config);
    return 0;
}
```

头文件

```c
//
// Created by aszswaz on 2021/3/17.
//

#ifndef DEMO_DEMO_H
#define DEMO_DEMO_H

#endif //DEMO_DEMO_H

static const int demo = 100;// 头文件中也可以声明变量

void demo();
```

函数实现文件

```c
//
// Created by aszswaz on 2021/3/17.
//
#include "stdio.h"

static int b = 10;

void demo() {
    printf("demo: %d\n", b);
}
```

直接使用 source 文件,但是不能编写函数, 只能声明一些变量, 可以用作程序常量配置

```c
//
// Created by aszswaz on 2021/3/17.
//

static const int config = 100;
```

## 连接属性

连接属性一共有三种: `external`(外部), `internal`(内部) 和 `none`(无)

没有连接属性的标识符(none)总是被当作单独的个体, 也就是说该标识符的多个声明被当作独立不同的实体.

属于internal连接的属性的标识符在同一个源文件内的所有声明中都指同一个实体, 但位于不同文件中的多个声明则分属不同的实体.

属于external连接属性的标识符, 不论被声明多少次, 位于几个源文件都表示同一个实体.

<span style="background-color: yellow">关键字: extern, static用于在声明中修改标识符的连接属性, 如果某个声明在正常情况下具有`external`属性, 在前面加上`static`关键字可以使它的连接属性变为'internal'.</span>

例: 创建三个文件: main.c,, demo.h, demo.c

```c
#include <stdio.h>
#include "demo.h"

int b;

int main() {
    demo();
    printf("main: %d\n", b);
    return 0;
}
```

```c
//
// Created by aszswaz on 2021/3/17.
//

#ifndef DEMO_DEMO_H
#define DEMO_DEMO_H

#endif //DEMO_DEMO_H

void demo();
```

```c
//
// Created by aszswaz on 2021/3/17.
//
#include "stdio.h"

int b = 10;

void demo() {
    printf("demo: %d\n", b);
}
```

这时, 代码中的两处`int b`的连接属性都是外部连接(external), 在编译会出现冲突:

```bash
$ gcc main.c demo.c demo.h -o demo
/usr/bin/ld: /tmp/ccPzT7B5.o:(.data+0x0): multiple definition of `b'; /tmp/ccCo2dRZ.o:(.bss+0x0): first defined here
collect2: 错误：ld 返回 1
```

须得使用`static`关键字将两个变量改为各自的文件私有

```c
static int b = 0;
```

修改后, 如下:

main.c

```c
#include <stdio.h>
#include "demo.h"

static int b;

int main() {
    demo();
    printf("main: %d\n", b);
    return 0;
}
```

dmoe.c

```c
//
// Created by aszswaz on 2021/3/17.
//
#include "stdio.h"

static int b = 100;

void demo() {
    printf("demo: %d\n", b);
}
```

```bash
$ gcc main.c demo.c demo.h -o demo

$ demo
demo: 100
main: 0
```

<span style="background-color: yellow">函数`demo()`在本题中也具有'external'属性, 同样也可以使用`static`修改为'intenal'</span>

<span style="color: red">对于在代码块内部声明的变量，如果给它加上关键字static，可以使它的存储类型从自动变为静态。具有静态存储类型的变量在整个程序的执行过程中一直存在，而不仅仅在声明它的代码块的执行时存在。<span style="background-color: yellow">注意：修改变量的存储类型并不表示修改该变量的作用域，它仍然只能在该代码块内部按名字访问</span></span>

<span style="color: green">最后：关键字`register`可以用于自动变量的声明，提示它们该存储于机器的硬件寄存器，而不是内存中，这类变量称为寄存器变量，如果一个编译器自己具有一套寄存器优化方法，它也可能忽略`register`</span>

## 初始化

除非对自动变量进行显式的初始化，否则当自动变量创建时，它们的值总是垃圾

```c
#include <stdio.h>

void demo();

int main() {
    int i;
    printf("main: %d\n", i);
    demo();
    return 0;
}

void demo() {
    int demo;
    printf("demo: %d\n", demo);
}
```

```bash
main: 0
demo: 32550 # demo每次运行的值都不一样
```

## static关键字

当用于不同上下文环境时，static关键字具有不同的意思

<span style="background-color: greenyellow">当它用于函数定义时，或用于代码块之外的变量声明时，static关键字用于修改表标识符的连接属性，从`external`改为`internal`，但标识符的存储类型和作用于不受影响。用这种方式声明的函数或变量只能在声明它们的源文件中访问。</span>

<span style="background-color: greenyellow">当它用于代码块内部的变量声明时，static关键字用于修改变量的存储类型，从自动变量修改为静态变量，但变量的连接属性和作用域不受影响。用这种方式声明的变量在程序执行之前创建，并在程序的整个执行期间一直存在，而不是每次在代码块开始执行时创建，在代码块执行完毕之后销毁。</span>

## 间接访问操作符

通过一个指针访问它所指向的地址的工程称为间接访问或解引用指针。操作符是单目操作符“*”

## 对特定的内存地址进行访问

假设变量a的内存地址是100，通过内存地址对a进行赋值，表达式如下：

```c
*(int *)100 = 25;
```

<span style="color: green">强制类型转换把100从“整型”转换为“指向整型的指针”，这样对它进行间接访问就是合法的。<span style="color: red">但是，需要使用这种技巧的机会绝无仅有的</span>，通常无法预测编译器会把某个特定的变量放在内存中的什么位置，所以无法预先知道它的地址。这个技巧的唯一有用之处是你偶尔需要通过地址访问内存中的某个特定位置，它并不是用于访问某个变量，而是访问硬件本身。例如，操作系统需要与输入输出设备控制器通信，启动I/O操作并从前面的操作中获得结果。这些位置必须通过它们的地址来访问，此时这些地址是预先已知的。</span>