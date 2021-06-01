#!/bin/sh

docker_run_v2ray(){
    sudo docker container stop $1
    sudo docker container rm $1
    sudo docker run -d -t --name=$1 -m=124M --memory-swap=-1 -v /etc/v2ray:/etc/v2ray -v /etc/hosts:/etc/hosts -p $2:$2 v2ray/official  v2ray -config=/etc/v2ray/$1.json
}
