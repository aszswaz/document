#|/bin/sh

bin="${HOME}/.local/share/elasticsearch"
jar="${bin}/elasticsearch-command-client.jar"
cache="${HOME}/.cache/elasticsearch"
config_dir="${HOME}/.config/elasticsearch"
bin_link="${HOME}/.local/bin/elasticsearch"

# 安装
if [ "$1" = "install" ]; then
  if mvn clean package -Dmaven.test.skip=true; then
    # 创建配置文件夹和程序安装目录
    if [ ! -r "${config_dir}" ]; then
      echo "mkdir ${config_dir}"
      mkdir "${config_dir}"
    fi
    if [ ! -r "${bin}" ]; then
      echo "mkdir ${bin}"
      mkdir "${bin}"
    fi
    if [ ! -r "${cache}" ]; then
      echo "mkdir ${cache}"
      mkdir "${cache}"
    fi
    # 复制jar包到程序的安装目录
    cp "target/elasticsearch-command-client.jar" "${jar}"
    # 复制脚本
    cp "elasticsearch.sh" "${bin}/elasticsearch.sh"
    # 添加链接
    ln "${bin}/elasticsearch.sh" "${bin_link}"
  fi
  exit 0
fi

# 卸载
if [ "$1" = "uninstall" ]; then
  if [ -r "${config_dir}" ]; then
    rm -rf "${config_dir}"
    echo "rm ${config_dir}"
  fi
  if [ -r "${bin}" ]; then
    rm -rf "${bin}"
    echo "rm ${bin}"
  fi
  if [ -r "${cache}" ]; then
    rm -rf "${cache}"
    echo "rm ${cache}"
  fi
  if [ -r "${bin_link}" ]; then
    rm -rf "${bin_link}"
    echo "rm ${bin_link}"
  fi
  exit 0
fi

# 不需要美化的指令
if [ "$1" = "version" ] || [ "$1" = "server" ]; then
  java -jar "${jar}" "$@"
  exit 0
fi

# 以下的指令需要缓存用的文件夹
if [ ! -r "${cache}" ]; then
  mkdir "${cache}"
fi

if [ "$1" = "indices" ]; then
  cache_file="${cache}/indices.txt"
  if java -jar "${jar}" "$@" >"${cache_file}"; then
    vim "${cache_file}"
  else
    # 退出程序并且返回上一个命令的返回值
    exit "$?"
  fi
else
  cache_file="${cache}/elasticsearch.json"
  if java -jar "${jar}" "$@" | jq >"${cache_file}"; then
    vim "${cache_file}"
  else
    # 退出程序并且返回上一个命令的返回值
    exit "$?"
  fi
fi

if [ -r "${cache}" ]; then
  rm -rf "${cache}"
fi
