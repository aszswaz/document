#!/bin/zsh

# 连接elasticsearch服务器，并且根据指定的参数，执行相应的查询操作
elasticsearch() {
  if [ "${ELASTICSEARCH_USERNAME}" = "" ]
  then
    base_comment="curl -X GET -s http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/path"
  else
    base_comment="curl -X GET -s http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/path --USER ${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}"
  fi
  editor="vim"
  default_query_file="query.json"
  elasticsearch_cache_dir="${HOME}/.cache/elasticsearch"
  if [ ! -x "${elasticsearch_cache_dir}" ]; then
    mkdir "${elasticsearch_cache_dir}"
  fi
  elasticsearch_cache_file="${elasticsearch_cache_dir}/elasticsearch_cache.json"

  # 输出帮助信息
  if [ "$#" -eq "0" ] || [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "-h --help : 输出帮助信息"
    echo "count : 聚合查询"
    echo "search : 搜索数据"
    echo "version: 查看服务器的版本信息"
    echo "indices: 查看服务器索引列表"
    return 0
  fi
  # 输出es服务器版本信息
  if [ "$1" = "version" ]; then
    eval "${base_comment/path/}" | jq
    return 0
  fi
  # 解析参数
  declare -a params # 声明数组
  for param in $@; do
    params+=("$param")
  done
  # 参数映射为字典
  declare -A params_dict
  i=1
  while [ "${i}" -le "${#params[@]}" ]; do
    str="${params[i]}"
    if [ "${str:0:1}" = "-" ]; then
      i=$(expr "${i}" + 1)

      if [ "$1" = "search" ] && [ "${str}" = "-q" ] && [ "${params[i]}" = "" ]; then
        ${editor} "${elasticsearch_cache_dir}/cache_query.json"
        params_dict["${str}"]="${elasticsearch_cache_dir}/cache_query.json"
        continue
      fi

      params_dict["${str}"]="${params[i]}"
    fi
    i=$(expr "${i}" + 1)
  done

  # 条件查询
  if [ "$1" = "search" ]; then
    if [ "${params_dict["-i"]}" = "" ]; then
      if [ "${params_dict["-q"]}" != "" ]; then
        curl_commend="${base_comment/path/_search} -H 'Content-Type: application/json' -d @${params_dict["-q"]}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        ${editor} "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      if [ -r "${default_query_file}" ]; then
        curl_commend="${base_comment/path/_search} -H 'Content-Type: application/json' -d @${default_query_file}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        ${editor} "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      curl_commend="${base_comment/path/_search}"
      eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
      ${editor} "${elasticsearch_cache_file}"
      rm -rf "${elasticsearch_cache_dir}"
      return 0
    fi
    if [ "${params_dict["-q"]}" = "" ]; then
      if [ -r "${default_query_file}" ]; then
        curl_commend="${base_comment/path/${params_dict["-i"]}/_search} -H 'Content-Type: application/json' -d @${default_query_file}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        ${editor} "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      curl_commend="${base_comment/path/${params_dict["-i"]}/_search}"
      eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
      ${editor} "${elasticsearch_cache_file}"
      rm -rf "${elasticsearch_cache_dir}"
      return 0
    fi
    curl_commend="${base_comment/path/${params_dict["-i"]}/_search} -H 'Content-Type: application/json' -d @${params_dict["-q"]}"
    eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
    ${editor} "${elasticsearch_cache_file}"
    rm -rf "${elasticsearch_cache_dir}"
    return 0
  fi

  # 聚合查询
  if [ "$1" = "count" ]; then
    if [ "${params_dict["-i"]}" != "" ]; then
      if [ "${params_dict["-q"]}" != "" ]; then
        curl_commend="${base_comment/path/${params_dict["-i"]}/_count} -H 'Content-Type: application/json' -d @${params_dict["-q"]}"
        eval "${curl_commend}" | jq >> "${elasticsearch_cache_file}"
        ${editor} "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0;
      else
        curl_commend="${base_comment/path/${params_dict["-i"]}/_count} -H 'Content-Type: application/json' -d @${default_query_file}"
        eval "${curl_commend}" | jq >> "${elasticsearch_cache_file}"
        ${editor} "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0;
      fi
    fi
  fi

  # 查看服务器的所有索引
  if [ "$1" = "indices" ]; then
    elasticsearch_cache_file="${elasticsearch_cache_dir}/indices.txt"
    curl_commend="${base_comment/path/_cat/indices}"
    eval "${curl_commend}" >> "${elasticsearch_cache_file}"
    ${editor} "${elasticsearch_cache_file}"
    rm "${elasticsearch_cache_file}"
  fi
}
