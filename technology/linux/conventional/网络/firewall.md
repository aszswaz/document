# 使用iptables管理防火墙

## 开放tcp端口

```bash
# 开放端口输入
$ sudo iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
# 开放端口输出
$ sudo iptables -A OUTPUT -p tcp --dport 8080 -j ACCEPT
```

查看规则是否添加成功

```bash
$ sudo iptables -L -n
```

```txt
Chain INPUT (policy ACCEPT)
target     prot opt source               destination         
ACCEPT     tcp  --  0.0.0.0/0            0.0.0.0/0            tcp dpt:8080

Chain FORWARD (policy ACCEPT)
target     prot opt source               destination         

Chain OUTPUT (policy ACCEPT)
target     prot opt source               destination         
ACCEPT     tcp  --  0.0.0.0/0            0.0.0.0/0            tcp dpt:8080
```

