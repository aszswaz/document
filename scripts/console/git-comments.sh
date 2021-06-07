#!/bin/sh
# git的自定义指令

git_push_all(){
    if git rev-parse; then
	    result=($(git remote))
        printf "检测到${#result}个远程仓库。\n"
        # 将代码提交到git的所有仓库
        for depository in $(git remote)
        do
            printf "正在提交: ${depository}\n"
            # 推送全部分支
            git push --all ${depository}
            git push --tags ${depository}
        done
        echo "完毕!"
    else
        echo "$(pwd)：不是git仓库！"
    fi
}
