# 宏

## define

define作用主要是进行替换。可替换的内容有两种：变量和代码块。

宏替换的过程叫做宏展开，gcc和gdb输出宏展开的指令如下：

gcc:

```bash
$ gcc -E demo.c -o demo.h
```

gdb:

```bash
(gdb) macro expan DEMO
```

替换变量到指定位置：

```c
#include "stdio.h"
#define DEMO_INT 110
#define DEMO_STRING "Hello World"

int main(void) {
    fprint(stdout, "DEMO_INT: %d\n", DEMO_INT);
    fprint(stdout, "DEMO_STRING: %s\n", DEMO_STRING);
    return 0;
}
```

宏展开之后是这样的：

```c
#include "stdio.h"

int main(void) {
    fprint(stdout, "DEMO_INT: %d\n", 110);
    fprint(stdout, "DEMO_STRING: %s\n", "Hello World");
    return 0;
}
```

替换代码块，以ffmpeg中的一段代码为例子：

```c
#define MATCH_PER_TYPE_OPT(name, type, outvar, fmtctx, mediatype)\
{\
    int i;\
    for (i = 0; i < o->nb_ ## name; i++) {\
        char *spec = o->name[i].specifier;\
        if (!strcmp(spec, mediatype))\
            outvar = o->name[i].u.type;\
    }\
}
```

宏调用处：

```c
MATCH_PER_TYPE_OPT(codec_names, str, audio_codec_name, ic, "a");
```

展开后：

```c
{
    int i;
    for (i = 0; i < o->nb_codec_names; i++) {
        char *spec = o->codec_names[i].specifier;
        if (!strcmp(spec, "a")) audio_codec_name = o->codec_names[i].u.str;
    }
}
```

