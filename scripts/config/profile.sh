#!/bin/sh

# 为了避免PATH被污染，重置PATH
export PATH=/home/aszswaz/.local/bin:/usr/local/sbin:/usr/local/bin:/usr/bin:/usr/bin/site_perl:/usr/bin/vendor_perl:/usr/bin/core_perl:/var/lib/snapd/snap/bin
# 基本环境配置
export MAVEN=/opt/aszswaz/maven
export JAVA_HOME=/opt/aszswaz/jdk
export CMAKE=/opt/aszswaz/cmake
export IDEA=/opt/aszswaz/idea
export MYSQL=/opt/aszswaz/mysql
export MYSQL_SHELL=/opt/aszswaz/mysql/shell
export PATH=${PATH}:${MAVEN}/bin:${JAVA_HOME}/bin:${CMAKE}/bin:${IDEA}/bin:${MYSQL}/bin:${MYSQL_SHELL}/bin

export PROJECT=/home/aszswaz/Project
export HTTP_PROXY='http://proxy.v2ray:10809'
export HTTPS_PROXY=${HTTP_PROXY}
export http_proxy=${HTTP_PROXY}
export https_proxy=${http_proxy}
export NO_PROXY='localhost,192.168.0.0/24,127.0.0.1,::1'
# 设置控制台语言
export LANG=zh_CN.UTF-8

services=(docker docker.socket);

# 运行开发环境的服务
start_dev_services(){
    for service in ${services}
    do
        echo "正在运行：${service}"
        sudo systemctl start ${service};
    done
}

# 重启服务
restart_dev_services(){
    for service in ${services}
    do
        echo "正在重新启动：${service}"
        sudo systemctl restart ${service};
    done
}

# 停止服务
stop_dev_services(){
    for service in ${services}
    do
        echo "正在停止：${service}"
        sudo systemctl stop ${service}
    done
}
