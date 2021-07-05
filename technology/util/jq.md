# jq，json format工具

## 格式化json

首先创建文件 demo.json

```json
{
    "key-01": "value-01",
    "key-02": "value-02"
}
```

格式化json，如果json的格式不对，会提示对应的错误信息

```bash
$ jq . demo.json
```

```json
{
  "key-01": "value-01",
  "key-02": "value-02"
}
```

## 获取所有的key

以上述的demo.json文件为例：

### 获得所有的key，并且作为json数组

```bash
$ jq 'keys' demo.json
```

```json
[
  "key-01",
  "key-02"
]
```

### 获得所有的key的值，相比上述：没有“[”，“]”和“,”

```bash
$ jq 'keys[]' demo.json
```

```bash
"key-01"
"key-02"
```

### 获得所有的key的值，相比上述：没有 " 和 "，是普通文本

```bash
$ jq -r 'keys[]' demo.json
```

```txt
key-01
key-02
```

