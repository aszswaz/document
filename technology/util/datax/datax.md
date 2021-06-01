### alibaba datax数据同步工具使用说明

### mysql数据同步配置模板

##### 指定表和列,由工具自动生成sql

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 3
      },
      "errorLimit": {
        "record": 0,
        "percentage": 0.02
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "username": "username",
            "password": "password",
            "column": [
              "id",
              "name"
            ],
            "splitPk": "db_id",
            "connection": [
              {
                "table": [
                  "table"
                ],
                "jdbcUrl": [
                  "jdbc:mysql://localhost:3306/database"
                ]
              }
            ]
          }
        },
        "writer": {
          "name": "streamwriter",
          "parameter": {
            "print": true
          }
        }
      }
    ]
  }
}
```

##### 使用自定义的sql进行同步

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 3,
        "byte": 1048576
      },
      "errorLimit": {
        "record": 0,
        "percentage": 0.02
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "username": "oPoExCTSlY/7GiCbHrwGow==",
            "password": "RS2l5M0Rmd3s6Q58vUnnoA==",
            "column": [
              "`id`",
              "`username`",
              "`password`",
              "`real_name`",
              "`email`",
              "`wechat_work`",
              "`note`",
              "`create_time`",
              "`privilege`",
              "`wechat_work_user_id`",
              "`record_creation_time`",
              "`record_modify_time`",
              "`record_founder`",
              "`record_modifier`"
            ],
            "splitPk": "",
            "connection": [
              {
                "table": [
                  "user_info"
                ],
                "jdbcUrl": [
                  "jdbc:mysql://localhost:3306/data_manage?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai"
                ]
              }
            ]
          }
        },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {
            "username": "username",
            "password": "password",
            "writeMode": "update (*)",
            "column": [
              "`id`",
              "`username`",
              "`password`",
              "`real_name`",
              "`email`",
              "`wechat_work`",
              "`note`",
              "`create_time`",
              "`privilege`",
              "`wechat_work_user_id`",
              "`record_creation_time`",
              "`record_modify_time`",
              "`record_founder`",
              "`record_modifier`"
            ],
            "connection": [
              {
                "table": [
                  "user_info"
                ],
                "jdbcUrl": "jdbc:mysql://localhost:3306/data_manage_sync?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai"
              }
            ]
          }
        }
      }
    ]
  }
}
```

<span style="background-color: yellow">"writeMode": "update (*)"配置可以指定datax的insert模式, 让数据在出现主键冲突时执行update操作</span>