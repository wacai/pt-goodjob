/**
 * Created by baojun on 16/3/26.
 */
module.exports = function (data, util) {
    var execlist = [];
    for (var i = 0; i < 50; i++) {
        var exec = {
            "createTime": "string",
            "end_time": 0,
            "exec_desc": "string",
            "exec_msg": "string",
            "exec_type": 0,
            "id": i,
            "lastUpdateTime": "string",
            "percent": "string",
            "sched_time": 0,
            "start_time": 0,
            "state": Math.floor(Math.random() * 2),
            "task_config_id": 0,
            "task_config_type": Math.floor(Math.random() * 3),
            "task_name": "taskname" + i
        };
        execlist.push(exec);
    }
    return execlist;
}
