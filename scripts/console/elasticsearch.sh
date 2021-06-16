#!/bin/bash

# 连接elasticsearch服务器，并且根据指定的参数，执行相应的查询操作
elasticsearch() {
  if [ "$#" -lt "1" ]; then
    curl -X GET -s "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" | jq
    return 0
  fi

  if [ "$1" = "search" ] || [ "$1" = "count" ]; then
    if [ "$#" -lt "2" ]; then
      echo "请输入索引名称" 1>&2
      return 1
    fi

    # 读取es的查询条件文件
    if [ "$#" -ge "3" ]; then
      query_file="$3"
      if [ ! -r "${query_file}" ]; then
        echo "文件：${query_file}不存在，或者无法读取" 1>&2
        return 1
      fi
      curl -X GET -s \
        "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$2/_$1" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" \
        -d "@${query_file}" \
        -H 'Content-Type: application/json' |
        jq
      return 0
    fi
    curl -X GET -s \
      "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$2/_$1" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" |
      jq
    return 0
  fi

  curl -X GET -s "http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/$1" --USER "${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}" | jq
}

# 获取elasticsearch服务器的所有索引
get_elasticsearch_indies() {
  array=()
  elasticsearch '_aliases' | jq keys |
    while read -r line; do
      line="${line/\[/}"
      line="${line/\"/}"
      line="${line/,/}"
      line="${line/\"/}"
      line="${line/\]/}"
      array+=("${line}")
    done
  for index in ${array[@]}; do
    echo "$index"
  done
}

# 按照查询条件，对索引进行聚合查询，默认查询全部的索引
elasticsearch_count() {
  count_number=0

  # 没有输入参数
  if [ "$#" -eq "0" ]; then
    get_elasticsearch_indies |
      while read -r line; do
        request=$(elasticsearch count "$line" | jq .count)
        if [ "$request" = "" ]; then
          return 1
        fi
        count_number=$(expr "${count_number}" + "${request}")
        echo "index: ${line}, count: ${request}"
      done
    return 0
  fi

  array=()
  for element in $@; do
    array+=("${element}")
  done

  # 参数字典
  declare -A dict
  i=1
  while [ "${i}" -lt "${#array[@]}" ]; do
    parameter="${array[i]}"
    if [ "${parameter:0:1}" = "-" ]; then
      i=$(expr "${i}" + 1)
      dict["${parameter}"]="${array[i]}"
    fi
    i=$(expr "${i}" + 1)
  done

  get_elasticsearch_indies |
    while read -r line; do
      if [ "${dict["-p"]}" = "" ]; then
        if [ "${dict["-q"]}" = "" ]; then
          request=$(elasticsearch count "$line" | jq .count)
        else
          request=$(elasticsearch count "$line" "${dict["-q"]}" | jq .count)
        fi
        echo "index: ${line}, count: ${request}"
        count_number=$(expr "${count_number}" + "${request}")
      else
        if [[ "$line" =~ "${dict["-p"]}" ]]; then
          if [ "${dict["-q"]}" = "" ]; then
            request=$(elasticsearch count "$line" | jq .count)
          else
            request=$(elasticsearch count "$line" "${dict["-q"]}" | jq .count)
          fi
          echo "index: ${line}, count: ${request}"
          count_number=$(expr "${count_number}" + "${request}")
        fi
      fi
    done

  echo "total count: ${count_number}"
}
