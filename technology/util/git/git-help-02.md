## git mode change

git 在记录文件的同时，也会记录文件的可执行权限。

演示操作如下：

初始化一个仓库

```bash
$ touch demo.txt
$ git init
$ git add -A
$ git commit -m "demo"
```

赋予文件可执行权限：

```bash
$ chmod u+x demo.txt
```

查看git

```bash
$ git status
```

```txt
位于分支 master
尚未暂存以备提交的变更：
  （使用 "git add <文件>..." 更新要提交的内容）
  （使用 "git restore <文件>..." 丢弃工作区的改动）
        修改：     demo.txt

修改尚未加入提交（使用 "git add" 和/或 "git commit -a"）
```

```bash
$ git diff demo.txt
```

```txt
diff --git a/demo.txt b/demo.txt
old mode 100644
new mode 100755
```

**解决办法**

禁用文件可执行权限的记录

```bash
$ git config --global core.fileMode
```

<span style="color: red">如果无效，可能是仓库本身的 git config 覆盖了 git config --global</span>

