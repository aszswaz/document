#!/bin/sh

# 获取elasticsearch服务器的所有索引
get_elasticsearch_indies(){
    elasticsearch '_aliases' | jq keys
}

# 连接elasticsearch服务器，并且根据指定的参数，执行相应的查询操作
elasticsearch(){
   curl -X GET -v "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$1" --USER ${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD} | jq
}
