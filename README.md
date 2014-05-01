解析流程中所有参数
============
startWorkFlow paramJson
-----------
    {
        "deptId":"",       //部门ID
        "paramKey":["",""], //参数键
        "paramValue":["",""] //参数值
    }

complete paramJson
-----------
    {
        "deptId": "",     //部门ID
        "owner": "",      //下个环节处理人  如果是多个人以英文“,”分割
        "paramKey": [     //参数键
            "",
            ""
        ],
        "paramValue": [   //参数值
            "",
            ""
        ],
        "workFlowCommentParameter": {       //意见
            "commentOption": "",   //意见选项 同意、不同意
            "content": ""          //意见内容
        }
    }

环节后台配置的JSON
------------
    {
        "tools": {
            "save": 1,            //保存
            "nextTaskPerson": 1,  //查看一下环节处理人
            "submit": 1,          //提交
            "back": 0,            //退回
            "follow": 1,          //流程跟踪
            "idea": [             //意见配置
                {
                    "optionText": "同意",
                    "optionValue": 1
                },
                {
                    "optionText": "不同意",
                    "optionValue": 2
                }
            ]
        },
        "nextTasks": [                       //下个环节流向处理
            {
                "taskName": "上级审批",       //环节名称
                "taskDefKey": "sjsp",        //环节定义的KEY
                "condition": "1",            //判断条件值即流转流转到此处需要此值
                "user": {                    //下个环节处理人
                    "isSelectUser": 0,       //是否弹出选择人员     0代表不选择  1代表选择
                    "isMultiple":0,          //是否多选            0代表单选  1代表多选
                    "userExp": "query.selectUsers(deptId , '1')"  //人员表达式
                }
            }
        ],
        "backTasks": [                       //退回
            {
                "taskName": "起草" ,      //环节名称
                "taskDefKey": "qc",        //环节定义的KEY
                "condition": "-1",            //判断条件值即流转流转到此处需要此值
            }
        ]
    }

环节后台配置中userExp解析后的JSON
---------------
    {
        "userName": "xxxx",
        "userId": "123456",
        "deptId": "123",
        "deptName": "xxxxx"
    }

解析后环节信息JSON
---------------
    [
        {
            "condition": "1",
            "taskDefKey": "userTask2",
            "taskName": "上级审批",
            "taskType": "1",
            "user": {
                "isMultiple": "0",
                "isSelectUser": "0",
                "userExp": "${users.getUsers('1')}",
                "workFlowParsedUsers": [
                    {
                        "deptId": "123",
                        "deptName": "部门",
                        "userId": "b",
                        "userName": "人员1"
                    },
                    {
                        "deptId": "123",
                        "deptName": "部门2",
                        "userId": "c",
                        "userName": "人员2"
                    }
                ]
            }
        }
]
