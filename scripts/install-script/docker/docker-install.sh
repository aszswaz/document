#!/bin/sh

# 安装docker
sudo yum -y install docker;
# 启动docker
sudo systemctl start docker;
sudo systemctl enable docker;

# 在docker中安装v2ray
install_v2ray(){
    sudo docker pull v2ray/official;
    config_dir="/etc/v2ray";
    # 创建配置文件
    sudo mkdir ${config_dir};
    sudo mv v2ray-config.json ${config_dir}/config.json;
    # 开启IP转发
    sudo sh -c "echo net.ipv4.ip_forward=1 >> /etc/sysctl.d/enable-ip-forward.conf";
    # 新容器运行, 注意：“:z”是表示自动获取SElinux的权限，--restart=always表示在docker服务启动时自动启动
    sudo docker run --restart=always -d --name v2ray -v /etc/v2ray:/etc/v2ray -p 10809:10809 v2ray/official  v2ray -config=/etc/v2ray/config.json;
    # 查看运行状态
    sudo docker container ls;
}

# 安装mysql
install_mysl(){
    # 获取镜像
    sudo docker pull mysql:5.7.25;
    # 创建容器
    sudo docker run -e TZ="Asia/Shanghai" -m 1G --memory-swap -1 --restart=always --privileged=true -d -v /home/mysql/:/var/lib/mysql:z -v /etc/mysql/:/etc/mysql/:z -p 3306:3306 --name mysqld -e MYSQL_ROOT_PASSWORD=root mysql:5.7.25;

    # 查看运行状态
    sudo docker container ls;
}

# 安装redis
install_redis(){
    sudo docker pull redis;
    # 创建并运行容器
    sudo docker run -d -p 6379:6379 -e TZ="Asia/Shanghai" --restart=always --privileged=true -v /home/redis/:/data:z -m 1G --memory-swap -1 --name redis redis --appendonly yes;
}

# 安装mongodb服务
install_mongodb(){
    sudo docker pull mongo;
    # --auth需要用户名访问
    sudo docker run -d -e Tz="Asia/Shanghai" -m 1G --memory-swap -1 --restart=always --privileged=true -v /home/mongo/:/data/db --name mongo -p 27017:27017 mongo --auth;
    # 创建用户, https://www.runoob.com/docker/docker-install-mongodb.html
    sudo docker exec -it mongo mongo admin;
    # db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
    # 验证连接
    # db.auth('admin', '123456');
}

install_v2ray;
