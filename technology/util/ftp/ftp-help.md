## 建立ftp连接

有以下两种方式

```bash
$ ftp 192.168.0.119
```

```bash
$ ftp
ftp> open 192.168.0.119
```

之后，不需要密码会直接登陆成功，需要密码的会提示输入账户名称和密码

## 查看当前路径

```bash
ftp> pwd
```

## 查看当前路径下的文件或文件夹

```bash
ftp> passive
Passive mode on.
ftp> dir
227 Entering Passive Mode (192,168,0,119,246,31).
125 Data connection already open; Transfer starting.
03-26-21  10:34AM       <DIR>          wxwork
226 Transfer complete.
```

## ftp设置传输二进制文件

```bash
ftp> bin
200 Type set to I.
ftp> get xxx.xlsx
```

