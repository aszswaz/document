# elasticsearch 数据库的命令行客户端工具

## 项目所需环境

**jdk** 版本至少需要8及以上

**maven** java的包管理，以及打包程序

**jq** 一款json格式化工具

**vim** 一款控制台的文本编译器

jq 和 vim 属于非必须工具，主要在 elasticsearch.sh 中调用，美化 es 的搜索结果，如果不需要，可以将其从脚本移除

## 安装

```shell
$ chmod u+x elasticsearch.sh
$ sh elasticsearch.sh install
```

## 卸载

```shell
$ sh elasticsearch.sh uninstall
```

## 更新

把jar包和elasticsearch.sh文件复制到 ${HOME}/.local/share/elasticsearch 文件夹下即可

```shell
$ cp target/elasticsearch-command-client.jar elasticsearch.sh "${HOME}/.local/share/elasticsearch"
```