#!/bin/sh

# 查看容器运行状态
docker_show(){
    docker -H ${server} stats $(docker -H ${server} ps --format={{.Names}});
}

# 查看容器的实时日志
docker_logs(){
    docker -H ${server} container logs -f --tail=40 $(docker_name);
}

# 根据当前文件夹和git的分支名称，生成docker的容器名称
docker_name(){
   # 这里的echo的作用是类似其他语言的return，shell的return只能返回数字，所以这里通过打印到标准输出流，来把结果返回给其他函数
   echo $(basename $(pwd))-$(git symbolic-ref --short -q HEAD);
}
