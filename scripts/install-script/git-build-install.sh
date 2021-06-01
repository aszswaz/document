#!/bin/sh

# 源码包名称
download_tar="git.zip";
builder_folder="/opt/git-builder";
base_folder="/opt/git";

git_install(){
    # 安装所需工具
    sudo yum makecache;
    sudo yum -y install wget unzip dnf epel-release;
    sudo dnf -y install dh-autoreconf curl-devel expat-devel gettext-devel openssl-devel perl-devel zlib-devel;
    sudo dnf -y install asciidoc xmlto docbook2X;
    # 确保二进制文件名一致
    sudo ln -s /usr/bin/db2x_docbook2texi /usr/bin/docbook2x-texi;

    # 下载压缩包
    if wget "https://github.com/git/git/archive/refs/heads/master.zip" -O ${download_tar}; then
        echo "安装包下载成功！开始编译";
        
        # 编译git
        git_building;
    else
        echo "安装包下载失败!";
    fi
}

# 编译git
git_building(){
    sudo mkdir ${builder_folder};

    if sudo unzip ${download_tar} -d ${builder_folder}; then
        rm ${download_tar};
        cd ${builder_folder};
        # 移动文件
        sudo mv git-*/* .;
        
        sudo mkdir ${base_folder};
        sudo make configure;
        sudo ./configure --prefix=${base_folder};
        sudo make all doc info;
        sudo make install install-doc install-html install-info;
        
        sudo ln ${base_folder}/bin/git /usr/local/bin/git;
        
        echo "正在清理文件";
        cd ${HOME};
        sudo rm -rf ${builder_folder};
    else
        echo "安装包解压失败！";
        echo "正在清理";
        sudo rm -rf ${builder_folder} ${base_folder};
        return;
    fi
}

git_install;
