# pacman包管理器教程

## 安装软件包

```zsh
# 只进行安装
sudo pacman -S xxx
# 先更新软件源再安装
sudo pacman -Sy xxx
```

## 查找软件包

```zsh
# 查找in镜像源仓库
pacman -Ss xxx
#  查找已经安装的软件
pacman -Qs xxx
```

## 删除软件包

```zsh
# 单个删除软件包
sudo pacman -R xxx
# 批量删除软件包，以ibus为例， 同时删除ibus和ibus-pinyin
sudo pacman -Rc ibus
```

## 根据下载速度排序镜像源

```bash
# 从官方镜像列表中获取200个最近同步过的源，并对这200个源进行大文件下载来，根据在你电脑里的下载速度进行排序，写入mirrorlist
$ reflector --verbose -l 200 -p http --sort rate --save /etc/pacman.d/mirrorlist
```

