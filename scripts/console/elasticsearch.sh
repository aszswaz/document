#!/bin/sh

# 获取elasticsearch服务器的所有索引
get_elasticsearch_indies() {
  elasticsearch '_aliases' | jq keys
}

# 连接elasticsearch服务器，并且根据指定的参数，执行相应的查询操作
elasticsearch() {
  if [ "$#" -lt "1" ]; then
    curl -X GET -v "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" | jq
    return 0
  fi

  if [ "$1" = "search" ] || [ "$1" = "count" ]; then
    if [ "$#" -lt "2" ]; then
      echo "请输入索引名称" 1>&2
      return 0
    fi

    # 读取es的查询条件文件
    query_file='query.json'
    if [ "$#" -ge "3" ]; then
      query_file="$3"
    fi
    echo "query: ${query_file}"

    if [ ! -r "${query_file}" ]; then
      echo "文件：${query_file}不存在，或者无法读取" 1>&2
      return 0
    fi

    curl -X GET -v \
      "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$2/_$1" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" \
      -d "@${query_file}" \
      -H 'Content-Type: application/json' |
      jq
    return 0
  fi

  curl -X GET -v "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$1" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" | jq
}
