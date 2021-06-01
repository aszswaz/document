# pacman

## 软件源配置

```bash
# 设置中国的源
$ sudo pacman-mirrors -i -c China -m rank # 之后会弹出框,进行选择即可
#更新系统软件
$ sudo pacman -Syu
```

之后还需要添加`archlinuxcn`源,不然很多软件找不到,编辑`/etc/pacman.conf`文件,添加

```bash
[archlinuxcn]
SigLevel = TrustAll
Server = https://mirrors.tuna.tsinghua.edu.cn/archlinuxcn/$arch
```



然后执行

```bash
$ sudo pacman -S archlinuxcn-keyring
```

然后就可以通过`pacman`命令安装各种软件了.

## 美化

### 主题

安装`numix cicle`图标

```
yay -S numix-circle-icon-theme-git
```

安装`Latte` dock软件

```
sudo pacman -S latte-dock
```

在`latte dock`软件启动后,右键`布局`->`配置`中选择`下载`,可以联网下载`macOS`主题,然后选择应用即可.

同理,在系统设置中,可以从互联网上下载自己喜欢的主题,图标,开机登录界面,锁屏等.

### 字体

在不修改字体渲染的情况下,各种软件的中文字体会大小不一,包括在使用`chrome`浏览网站时.所以需要更改默认的字体渲染,在尝试微软雅黑等字体后,个人觉得思源黑体比较适合

#### 安装思源黑体

```bash
# 文泉驿黑
$ sudo pacman -S wqy-bitmapfont wqy-microhei wqy-microhei-lite wqy-zenhei
# 思源字体
$ sudo pacman -S noto-fonts-cjk adobe-source-han-sans-cn-fonts adobe-source-han-serif-cn-fonts
```

#### 复制`Windows`下的字体至`/usr/share/fonts`文件夹下

参考链接:
https://wiki.archlinux.org/index.php/Microsoft_fonts

[中文版](https://wiki.archlinux.org/index.php/Microsoft_fonts_(简体中文)

#### 修改渲染文件

1. 在`/etc/fonts`下新建`local.conf`文件

   ```xml
   <fontconfig>
       <match target="font">
           <edit name="autohint"> 
               <bool>false</bool>            
           </edit> 
           <edit name="hinting">
               <bool>false</bool>
           </edit>
           <edit name="hintstyle">
               <const>hintnone</const>
           </edit>
       </match>
       
       <match target="pattern">
           <test qual="any" name="family">
               <string>sans</string>
           </test>
           <edit name="family" mode="assign" binding="same">
               <string>Yahei Mono</string>
           </edit>
       </match>
       
       <match target="pattern">
           <test qual="any" name="family">
               <string>serif</string>
           </test>
           <edit name="family" mode="assign" binding="same">
               <string>Source Han Sans CN</string>
           </edit>
       </match>
       
       <match target="pattern">
           <test qual="any" name="family">
               <string>sans serif</string>
           </test>
           <edit name="family" mode="assign" binding="same">
               <string>Source Han Sans CN</string>
           </edit>
       </match>
       
       <match target="pattern">
           <test qual="any" name="family">
               <string>sans-serif</string>
           </test>
           <edit name="family" mode="assign" binding="same">
               <string>Source Han Sans CN</string>
           </edit>
       </match>
       
       <match target="pattern">
           <test qual="any" name="family">
               <string>monospace</string>
           </test>
           <edit name="family" mode="assign" binding="same">
               <string>Source Han Sans CN</string>
           </edit>
       </match>
   </fontconfig>
   ```

2. 修改`/etc/fonts/conf.d/69-language-selector-zh-cn.conf`文件,添加`思源黑体`在第一位

   ```xml
   <string>Source Han Sans CN</string>
   ```

   最终文件内容

   ```xml
   <?xml version="1.0"?>
   <!DOCTYPE fontconfig SYSTEM "fonts.dtd">
   <fontconfig>
   
           <match target="pattern">
           <test name="lang">
               <string>zh-cn</string>
           </test>
                   <test qual="any" name="family">
                           <string>serif</string>
                   </test>
                   <edit name="family" mode="prepend" binding="strong">
                           <string>Source Han Sans CN</string>
                           <string>Microsoft YaHei</string>
                           <string>Simsun</string>
                           <string>Droid Sans</string>
                           <string>HYSong</string>
                           <string>AR PL UMing CN</string>
                           <string>AR PL UMing HK</string>
                           <string>AR PL New Sung</string>
                           <string>WenQuanYi Bitmap Song</string>
                           <string>AR PL UKai CN</string>
                           <string>AR PL ZenKai Uni</string>
                   </edit>
           </match> 
           <match target="pattern">
                   <test qual="any" name="family">
                           <string>sans-serif</string>
                   </test>
                   <edit name="family" mode="prepend" binding="strong">
                           <string>Source Han Sans CN</string>
                           <string>Microsoft YaHei</string>
                           <string>Simsun</string>
                           <string>Droid Sans</string>
                           <string>WenQuanYi Zen Hei</string>
                           <string>HYSong</string>
                           <string>AR PL UMing CN</string>
                           <string>AR PL UMing HK</string>
                           <string>AR PL New Sung</string>
                           <string>AR PL UKai CN</string>
                           <string>AR PL ZenKai Uni</string>
                   </edit>
           </match> 
           <match target="pattern">
                   <test qual="any" name="family">
                           <string>monospace</string>
                   </test>
                   <edit name="family" mode="prepend" binding="strong">
                           <string>Monospace</string>
                           <string>Ubuntu Mono</string>
                           <string>DejaVu Sans Mono</string>
                           <string>Oxygen Mono</string>
                           <string>Microsoft YaHei</string>
                           <string>Simsun</string>
                           <string>Droid Sans</string>
                           <string>WenQuanYi Zen Hei Mono</string>
                           <string>HYSong</string>
                           <string>AR PL UMing CN</string>
                           <string>AR PL UMing HK</string>
                           <string>AR PL New Sung</string>
                           <string>AR PL UKai CN</string>
                           <string>AR PL ZenKai Uni</string>
                   </edit>
           </match> 
   
   </fontconfig>
   ```

1. 下边文件可以不配置,做个备份

   `~/.config/fontconfig/fonts.conf`文件内容:

```xml
<?xml version='1.0'?>
<!DOCTYPE fontconfig SYSTEM 'fonts.dtd'>
<fontconfig>
 <its:rules version="1.0" xmlns:its="http://www.w3.org/2005/11/its">
  <its:translateRule selector="/fontconfig/*[not(self::description)]" translate="no"/>
 </its:rules>
 <!-- 
 Artificial oblique for fonts without an italic or oblique version
 -->
 <match target="font">
  <!-- check to see if the font is roman -->
  <test name="slant">
   <const>roman</const>
  </test>
  <!-- check to see if the pattern requested non-roman -->
  <test name="slant" compare="not_eq" target="pattern">
   <const>roman</const>
  </test>
  <!-- multiply the matrix to slant the font -->
  <edit name="matrix" mode="assign">
   <times>
    <name>matrix</name>
    <matrix>
     <double>1</double>
     <double>0.2</double>
     <double>0</double>
     <double>1</double>
    </matrix>
   </times>
  </edit>
  <!-- pretend the font is oblique now -->
  <edit name="slant" mode="assign">
   <const>oblique</const>
  </edit>
  <!-- and disable embedded bitmaps for artificial oblique -->
  <edit name="embeddedbitmap" mode="assign">
   <bool>false</bool>
  </edit>
 </match>
 <!--
 Synthetic emboldening for fonts that do not have bold face available
 -->
 <match target="font">
  <!-- check to see if the weight in the font is less than medium which possibly need emboldening -->
  <test name="weight" compare="less_eq">
   <const>medium</const>
  </test>
  <!-- check to see if the pattern requests bold -->
  <test name="weight" compare="more_eq" target="pattern">
   <const>bold</const>
  </test>
  <!--
                  set the embolden flag
                  needed for applications using cairo, e.g. gucharmap, gedit, ...
                -->
  <edit name="embolden" mode="assign">
   <bool>true</bool>
  </edit>
  <!--
                 set weight to bold
                 needed for applications using Xft directly, e.g. Firefox, ...
                -->
  <edit name="weight" mode="assign">
   <const>bold</const>
  </edit>
 </match>
 <match target="font">
  <edit name="hinting" mode="assign">
   <bool>true</bool>
  </edit>
 </match>
 <match target="font">
  <edit name="hintstyle" mode="assign">
   <const>hintfull</const>
  </edit>
 </match>
 <dir>~/.fonts</dir>
 <match target="font">
  <edit name="rgba" mode="assign">
   <const>vbgr</const>
  </edit>
 </match>
 <match target="font">
  <edit name="antialias" mode="assign">
   <bool>true</bool>
  </edit>
 </match>
</fontconfig>
```