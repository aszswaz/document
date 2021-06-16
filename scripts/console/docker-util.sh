#!/bin/sh

# 查看容器运行状态
docker_show() {
  docker -H "${TEST_SERVER}" stats
}
