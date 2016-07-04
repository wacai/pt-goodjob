/**
 * Created by baojun on 16/4/4.
 */
module.exports = function (data, util) {
    var arrs = [];

    for (var i = 0; i < 20; i++) {
        arrs.push({
            'id': i,
            'task_name': 'taskName' + i
        });
    }

    return arrs;
}
