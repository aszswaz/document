#!/bin/sh

# 获取elasticsearch服务器的所有索引
get_elasticsearch_indies(){
    curl -X GET -v "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/_aliases" --USER ${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD} | jq keys
}
