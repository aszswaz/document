# curl

## 使用代理访问网站

**指令格式：**

```bash
$ curl -x [protocol]://[host]:[port] [url]
```

支持的代理协议：

| 协议    | 说明                                                         |
| ------- | ------------------------------------------------------------ |
| http    | 基于http的代理                                               |
| https   | 基于https的代理                                              |
| SOCKS4  | 只支持TCP应用；                                              |
| SOCKS4A | 支持TCP应用；支持服务器端域名解析；                          |
| SOCKS5  | 支持TCP和UDP应用；支持服务器端域名解析（curl需要使用socks5h）；支持多种身份验证；支持IPV6； |
| SOCKS5h | 支持socks5全部功能，支持服务端域名解析，仅curl支持           |

**例：**

以百度为例，使用socks5代理访问百度，指令如下：

```bash
$ curl -x socks5://example.com:80 https://www.baidu.com
```

<font color="red">注意，使用socks5链接时，域名的解析仍然是由本机来完成的</font>

修改hosts文件，加入如下内容

```bash
127.0.0.1  www.baidu.com
```

再次请求

```bash
$ curl -x socks5://example.com:80 https://www.baidu.com
```

```txt
curl: (35) OpenSSL SSL_connect: SSL_ERROR_SYSCALL in connection to www.baidu.com:443 
```

这个问题在多数时候不会造成什么后果，但是有一种情况是例外的，那就是域名被污染了，比如访问`www.google.com`

```bash
$ curl -x socks5://example.com:80 https://www.google.com
```

```txt
curl: (60) SSL: no alternative certificate subject name matches target host name 'www.google.com'
More details here: https://curl.se/docs/sslcerts.html

curl failed to verify the legitimacy of the server and therefore could not
establish a secure connection to it. To learn more about this situation and
how to fix it, please visit the web page mentioned above.
```

<font color="green">将协议从socks5改为socks5h即可</font>