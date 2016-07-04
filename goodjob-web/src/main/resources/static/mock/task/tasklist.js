/**
 * Created by baojun on 16/3/25.
 */
module.exports = function (data, util) {
    var tasklist = [];
    for (var i = 1; i <= 50; i++) {
        var task = {
            id: i,
            task_name: "taskName" + i,
            task_desc: "taskDesc" + i,
            task_state: Math.floor(Math.random() * 2),
            task_group: "group" + i,
            project_id: Math.floor(Math.random() * 4),
            project_name: "project_name" + i,
            cron_exp: "yyyy,mm,dd",
            "delay_skip": 0,
            "exact_once": 0,
            lastModifyBy: "",
            lastUpdateTime: "",
            next_fire_time: (new Date).valueOf(),
            prev_fire_time: (new Date).valueOf(),
            type: Math.floor(Math.random() * 3),
            job_data: "{a:b}"
        };
        tasklist.push(task);
    }
    return tasklist;
}
