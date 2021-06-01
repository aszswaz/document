## 引入整个文件夹

```cmake
include_directories(/thirdparty/comm/include)
```

## 编译动态链接库

```cmake
# 设置so文件输出文件夹
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/cmake-build-debug/so)
# 添加编译文件
# zhong_jni_JniDemo 是文件名称,最终生成的文件名称是 libzhong_jni_JniDemo.so
# SHARED 库被动态链接并在运行时加载
add_library(zhong_jni_JniDemo SHARED zhong_jni_JniDemo.h zhong_jni_JniDemo.c)
```

| 库类型 | 作用                                                         |
| ------ | ------------------------------------------------------------ |
| STATIC | 库是目标文件的存档，供链接其他目标时使用。                   |
| SHARED | 库被动态链接并在运行时加载。                                 |
| MODULE | 库是未链接到其他目标的插件，但可以使用类似dlopen的功能在运行时动态加载。 |

## 文件编码

```cmake
# 开启UNICODE支持，可以在代码中给字符串添加L，表示使用UNICODE编码
add_definitions(-DUNICODE -D_UNICODE)
add_definitions(-D_AFXDLL)
```

## 打印信息

```cmake
aMESSAGE(STATUS "JDK: $ENV{JAVA_HOME}")
```

<span style='color: red'>注意MASSAGE和STATUS一定要大写</span>，STATUS表示这是一条普通的信息，另外还有ERROR表示错误信息，<span style="color: green">$ENV{}：这个表示引用环境变量，本例子使用了JAVA_HOME变量</span>

## 添加模块

<span style="color: green">cmake 提供了很多第三方库的引入方式，可以使用`cmake --help-module-list `查看支持的库</span>

以ALSA为例

```cmake
cmake_minimum_required(VERSION 3.19)
project(demo C)

set(CMAKE_C_STANDARD 99)

add_executable(${PROJECT_NAME} main.c)

# 请求寻找ALSA库
find_package(ALSA REQUIRED)
# 寻找成功 ALSA_FOUND 为 true 否则为 false
if(ALSA_FOUND)
	# 寻找成功，连接ALSA的库文件夹
    include_directories(${ALSA_INCLUDE_DIRS})
    # 连接文件
    target_link_libraries(${PROJECT_NAME} ${ALSA_LIBRARIES})
endif(ALSA_FOUND)
```

