#!/bin/zsh

base_comment="curl -X GET -s http://${ELASTICSEARCH_SERVER}:${ELASTICSEARCH_PORT}/path --USER ${ELASTICSEARCH_USERNAME}:${ELASTICSEARCH_PASSWORD}"

# 连接elasticsearch服务器，并且根据指定的参数，执行相应的查询操作
elasticsearch() {
  # 输出帮助信息
  if [ "$#" -eq "0" ] || [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "-h --help : 输出帮助信息"
    echo "count : 聚合查询"
    echo "search : 搜索数据"
  fi
  # 输出es服务器版本信息
  if [ "$1" = "version" ]; then
    eval "${base_comment/path/}" | jq
    return 0
  fi
  # 解析参数
  params=()
  for param in $@; do
    params+=("$param")
  done
  # 参数映射为字典
  declare -A params_dict
  i=0
  while [ "${i}" -lt "${#params[@]}" ]; do
    str="${params[i]}"
    if [ "${str:0:1}" = "-" ]; then
      i=$(expr "${i}" + 1)
      params_dict["${str}"]="${params[i]}"
    fi
    i=$(expr "${i}" + 1)
  done

  elasticsearch_cache_dir="${HOME}/.cache/elasticsearch"
  if [ ! -x "${elasticsearch_cache_dir}" ]; then
    mkdir "${elasticsearch_cache_dir}"
  fi
  elasticsearch_cache_file="${elasticsearch_cache_dir}/elasticsearch_cache.json"

  default_query_file="query.json"

  if [ "$1" = "search" ]; then
    if [ "${params_dict["-i"]}" = "" ]; then
      if [ "${params_dict["-q"]}" != "" ]; then
        curl_commend="${base_comment/path/_search} -H 'Content-Type: application/json' -d @${params_dict["-q"]}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        vim "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      if [ -r "${default_query_file}" ]; then
        curl_commend="${base_comment/path/_search} -H 'Content-Type: application/json' -d @${default_query_file}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        vim "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      curl_commend="${base_comment/path/_search}"
      eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
      vim "${elasticsearch_cache_file}"
      rm -rf "${elasticsearch_cache_dir}"
      return 0
    fi
    if [ "${params_dict["-q"]}" = "" ]; then
      if [ -r "${default_query_file}" ]; then
        curl_commend="${base_comment/path/${params_dict["-i"]}/_search} -H 'Content-Type: application/json' -d @${default_query_file}"
        eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
        vim "${elasticsearch_cache_file}"
        rm -rf "${elasticsearch_cache_dir}"
        return 0
      fi
      curl_commend="${base_comment/path/${params_dict["-i"]}/_search}"
      eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
      vim "${elasticsearch_cache_file}"
      rm -rf "${elasticsearch_cache_dir}"
      return 0
    fi
    curl_commend="${base_comment/path/${params_dict["-i"]}/_search} -H 'Content-Type: application/json' -d @${params_dict["-q"]}"
    eval "${curl_commend}" | jq >>"${elasticsearch_cache_file}"
    vim "${elasticsearch_cache_file}"
    rm -rf "${elasticsearch_cache_dir}"
    return 0
  fi
}
