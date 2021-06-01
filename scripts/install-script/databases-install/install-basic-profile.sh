#!/usr/bin/sh

# 脚本启动入口
base_folder="/opt/databases";
echo "create folder: ${base_folder}";
sudo mkdir ${base_folder};
# 获取当前的脚本所在目录
current_dir=$(cd `dirname $0`; pwd)

main(){
    install_mysql;
    install_mongdb;
    install_redis;
}

# 安装mysql数据库
install_mysql(){
    cd ${current_dir};
     # 删除MariaDB
    echo "remove MariaDB";
    sudo yum -y remove mari*;
    sudo rm -rf /var/lib/mysql;
    
    sudo yum makecache;
    sudo yum -y install libaio;
    
    mysql_folder="${base_folder}/mysql";
    echo "create folder: ${mysql_folder}";
    sudo mkdir "${mysql_folder}";
    sudo tar -zxvf mysql.tar.gz -C ${mysql_folder};
    # 复制服务文件
    echo "create service..."
    sudo cp mysql.service /usr/lib/systemd/system/;
    config_file="my.cnf"
    config_file_path="${mysql_folder}/${config_file}"
    echo "copy ${config_file} to ${config_file_path}"
    sudo cp my.cnf "${mysql_folder}/"
    cd "${mysql_folder}";
    echo "mv and rm mysql-5.7.25-linux-glibc2.12-x86_64";
    sudo mv mysql*/* .;
    sudo rm -r mysql-*;
    echo "create mysql user";
    sudo groupadd mysql;
    sudo useradd -r -g mysql -s /bin/false mysql;
    # 创建软连接
    echo "create Soft connection";
    cd /usr/local;
    sudo ln -s ${mysql_folder} mysql;
    # 开始初始化数据库
    cd mysql;
    data_dir="${mysql_folder}/data"
    echo "create data dir ${data_dir}"
    sudo mkdir data;
    sudo mkdir mysql-files;
    sudo chown mysql:mysql mysql-files;
    sudo chmod 750 mysql-files;
    
    if sudo bin/mysqld --defaults-file=${config_file_path} --initialize-insecure; then
        sudo chown -R mysql:mysql .
        sudo bin/mysqld_safe --user=mysql &
        # 创建rsa key
        sudo bin/mysql_ssl_rsa_setup --datadir=${data_dir}
        echo "mysql installed, starting mysql";
        sudo systemctl enable mysql;
        sudo systemctl start mysql;
        # 创建硬连接
        sudo ln bin/mysql /usr/local/bin/mysql;
    fi
}

# 安装mongodb数据库
install_mongdb(){
    cd ${current_dir};
    sudo yum -y install libcurl openssl xz-libs;
    
    # 创建文件夹
    mongodb_path="${base_folder}/mongodb";
    mongodb_path_ln="/usr/local/mongodb";
    echo "create dir ${mongodb_path}";
    sudo mkdir ${mongodb_path};
    sudo ln -s ${mongodb_path} ${mongodb_path_ln};
    
    sudo tar -zxvf mongodb.tar.gz -C ${mongodb_path_ln};
    sudo cp mongod.conf /etc/;
    sudo cp mongod.service /usr/lib/systemd/system/;
    
    cd ${mongodb_path_ln};
    sudo mv mongodb*/* .;
    sudo rm -r mongodb-*;
    
    # 创建数据目录
    sudo mkdir ${mongodb_path_ln}/data/
    
    # 创建mongodb用户
    sudo groupadd mongod;
    sudo useradd -r -g mongod -s /bin/false mongod;
    sudo chown -R mongod:mongod ${mongodb_path_ln};
    
    sudo systemctl enable mongod;
    sudo systemctl start mongod;
    
    # 创建连接
    sudo ln ${mongodb_path_ln}/bin/mongo /usr/local/bin/mongo
}

# 安装redis
install_redis(){
    cd ${current_dir};
    sudo yum -y install gcc;
    # 目标文件夹
    redis_path="${base_folder}/redis";
    redis_ln="/usr/local/bin/redis";
    
    # 安装前的准备
    echo "create dir ${redis_path}";
    sudo mkdir ${redis_path};
    
    # 开始安装
    sudo tar -zxvf redis.tar.gz -C ${redis_path};
    sudo mv ${redis_path}/redis-*/* ${redis_path};
    sudo rmdir ${redis_path}/redis-*;
    
    # 开始编译
    cd ${redis_path};
    sudo make;
    sudo make install;
    
    # 注册服务，替换配置文件
    cd ${current_dir};
    sudo mv redis.service /usr/lib/systemd/system/;
    sudo rm ${redis_path}/redis.conf;
    sudo mv redis.conf ${redis_path}/redis.conf;
    
    # 开机启动服务
    sudo systemctl enable redis;
    sudo systemctl start redis;
}

main;

