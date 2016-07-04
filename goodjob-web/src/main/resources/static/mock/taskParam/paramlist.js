/**
 * Created by baojun on 16/4/5.
 */
module.exports = function (data, util) {

    var arrs = [], taskConfigId = data.body.taskConfigId || 1;
    for (var i = 1; i <= 3; i++) {
        arrs.push({
            "createTime": "string",
            "id": i,
            "lastModifyBy": "string",
            "lastUpdateTime": "string",
            "param": "param" + i,
            "task_config_id": taskConfigId
        })
    }
    return arrs;
}
