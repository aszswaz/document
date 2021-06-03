# C标准函数库 stdio.h

## setvbuf

函数原型

```c
int setvbuf(FILE *stream, char *buffer, int mode, size_t size)
```

setvbuf定义应该如何缓冲输出流

**参数**

-   **stream** -- 这是指向 FILE 对象的指针，该 FILE 对象标识了一个打开的流。
-   **buffer** -- 这是分配给用户的缓冲。如果设置为 NULL，该函数会自动分配一个指定大小的缓冲。
-   **mode** -- 这指定了文件缓冲的模式：

| 模式   | 描述                                                         |
| :----- | :----------------------------------------------------------- |
| _IOFBF | **全缓冲**：对于输出，数据在缓冲填满时被一次性写入。对于输入，缓冲会在请求输入且缓冲为空时被填充。 |
| _IOLBF | **行缓冲**：对于输出，数据在遇到换行符或者在缓冲填满时被写入，具体视情况而定。对于输入，缓冲会在请求输入且缓冲为空时被填充，直到遇到下一个换行符。 |
| _IONBF | **无缓冲**：不使用缓冲。每个 I/O 操作都被即时写入。buffer 和 size 参数被忽略。 |

**返回值**

如果成功，则该函数返回 0，否则返回非零值。

**实例**

```c
#include <stdio.h>
#include <unistd.h>
#include <string.h>

int main() {

    char buff[1024];

    // 开辟内存
    memset(buff, '\0', sizeof(buff));

    fprintf(stdout, "启用全缓冲\n");
    setvbuf(stdout, buff, _IOFBF, 1024);

    fprintf(stdout, "这里是 example.com\n");
    fprintf(stdout, "该输出将保存到 buff\n");
    // 刷新缓冲区
    fflush(stdout);

    // 程序退出时会刷新输出流
    fprintf(stdout, "这将在编程时出现\n");
    fprintf(stdout, "最后休眠五秒钟\n");

    sleep(5);

    return (0);
}
```

```txt
启用全缓冲
这里是 example.com
该输出将保存到 buff
这将在编程时出现
最后休眠五秒钟
```

