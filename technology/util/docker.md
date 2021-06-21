# Docker

## docker怎么修改容器的时间

以普通方式运行docker容器

```undefined
docker run -it --rm --name centos centos /bin/bash
```

参数说明：

```undefined
-it: 表示启用一个伪终端,并以交互方式运行
--rm: 表示退出之后立马删除该容器
--name: 表示给容器起一个名字
centos: 表示镜像名称
/bin/bash: 表示运行于bash程序
```

此时进入容器之后执行修改容器时间的指令`date +%T -s "15:03:00"`会给出`date: cannot set date: Operation not permitted`的错误

那么如何才能修改容器的时间呢? 主要是因为有时候需要测试一些定时任务的场景

退出之前的容器, 以如下方式重新进入容器：

```
docker run -it --cap-add SYS_TIME --rm --name centos centos /bin/bash
```

进入容器再使用指令`date +%T -s "15:03:00", 修改时间, 此时就可以修改成功了。

这个和之前的方式有什么区别呢?多了`--cap-add SYS_TIME`参数

由于docker容器的隔离是基于Linux的Capability机制实现的, Linux的Capability机制允许你将超级用户相关的高级权限划分成为不同的小单元. 目前Docker容器默认只用到了以下的Capability。

```objectivec
CHOWN, 
DAC_OVERRIDE, 
FSETID, 
FOWNER, 
MKNOD, 
NET_RAW, 
SETGID,  
SETUID, 
SETFCAP, 
SETPCAP, 
NET_BIND_SERVICE, 
SYS_CHROOT, 
KILL, 
AUDIT_WRITE
```

而要修改系统时间需要有`SYS_TIME`权限。使用 `--cap-add`, `--cap-drop` 可以添加或禁用特定的权限。

`--privileged`参数也可以达到开放权限的作用, 与`--cap-add`的区别就是, `--privileged`是将所有权限给容器。

docker使用`--privileged`, `--cap-add`, `--cap-drop` 来对容器本身的能力进行开放或限制。