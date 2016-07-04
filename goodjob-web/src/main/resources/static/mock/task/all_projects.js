/**
 * Created by baojun on 16/4/11.
 */
module.exports = function (data, util) {
    var plist = [];
    for (var i = 1; i <= 10; i++) {
        plist.push({
            "code": "code" + i,
            "id": i,
            "name": "name" + i,
            "p_desc": "desc" + i
        });
    }
    return plist;
}
