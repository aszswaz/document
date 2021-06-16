#!/bin/bash
# git的自定义指令

git_push_all() {
  if git rev-parse; then
    array=()
    # shellcheck disable=SC2030
    git remote | while IFS="" read -r line; do array+=("${line}"); done
    # shellcheck disable=SC2031
    echo "检测到${#array}个远程仓库。"
    # 将代码提交到git的所有仓库
    # shellcheck disable=SC2031
    for depository in "${array[@]}"; do
      echo "正在提交: ${depository}"
      git push "${depository}"
      git push --tags "${depository}"
    done
    echo "完毕!"
  else
    echo "$(pwd)：不是git仓库！"
  fi
}
