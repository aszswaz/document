# C 标准库 - string.h

## strcmp()

函数原型

```c
int strcmp(const char *str1, const char *str2);
```

### 参数

-   **str1** -- 要进行比较的第一个字符串。
-   **str2** -- 要进行比较的第二个字符串。

### 返回值

该函数返回值如下：

-   如果返回值小于 0，则表示 str1 小于 str2。
-   如果返回值大于 0，则表示 str1 大于 str2。
-   如果返回值等于 0，则表示 str1 等于 str2。

### 示例

```c
#include <stdio.h>
#include <string.h>
 
int main ()
{
   char str1[15];
   char str2[15];
   int ret;
 
 
   strcpy(str1, "abcdef");
   strcpy(str2, "ABCDEF");
 
   ret = strcmp(str1, str2);
 
   if(ret < 0)
   {
      printf("str1 小于 str2");
   }
   else if(ret > 0) 
   {
      printf("str1 大于 str2");
   }
   else 
   {
      printf("str1 等于 str2");
   }
   
   return(0);
}
```

让我们编译并运行上面的程序，这将产生以下结果：

```txt
str1 大于 str2
```

### 网友笔记

```txt
两个字符串自左向右逐个字符相比（按 ASCII 值大小相比较），直到出现不同的字符或遇 \0 为止。如：

1."A"<"B" 
2."A"<"AB" 
3."Apple"<"Banana" 
4."A"<"a" 
5."compare"<"computer"
特别注意：strcmp(const char *s1,const char * s2) 这里面只能比较字符串，即可用于比较两个字符串常量，或比较数组和字符串常量，不能比较数字等其他形式的参数。

ANSI 标准规定，返回值为正数，负数，0 。而确切数值是依赖不同的C实现的。

当两个字符串不相等时，C 标准没有规定返回值会是 1 或 -1，只规定了正数和负数。

有些会把两个字符的 ASCII 码之差作为比较结果由函数值返回。但无论如何不能以此条依据作为程序中的流程逻辑。
```

