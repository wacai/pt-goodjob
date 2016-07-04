/**
 * Created by baojun on 16/4/4.
 */
module.exports = function (data, util) {

    var arrs = [];
    for (var i = 0; i < 3; i++) {
        arrs.push({
            "createTime": "2011-11-11 11:11:11",
            "end_time": '2011-11-11 11:11:11',
            "error_msg": "errMsg",
            "host_ip": "127.0.0.1",
            "id": i,
            "job_group": "string",
            "job_name": "string",
            "job_param": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "lastUpdateTime": "string",
            "start_time": 0,
            "state": Math.floor(Math.random() * 6),
            "task_exec_id": 1
        });
    }
    return arrs;
}
