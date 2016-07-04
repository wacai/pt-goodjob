/**
 * Created by baojun on 16/4/1.
 */
template.config("compress", true);
template.config("cache", false);


$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();

    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || null);

        } else {
            o[this.name] = this.value || null;
        }
    });
    return o;
};

$(document).ajaxSend(function () {
    NProgress.start();
});
$(document).ajaxComplete(function () {
    NProgress.done();
});

/**
 * 读取当前页面的模板id
 * @param tplId
 * @param data
 * @returns {*}
 */
function renderContentByTpl(tplId, data) {
    var html = template(tplId, data);
    return html;
}

/**
 *读取模板文件
 * @param {Object} url
 */
function readTemplate(url) {
    var df = new $.Deferred();
    $.ajax({
        url: url
    }).done(function (data) {
        var render = template.compile(data);
        df.resolve(render);
    }).fail(function () {
        $.error('读取外部模板出错');
        notie.alert(3, '读取外部模板出错', 2.5);
    })
    return df.promise();
}


//对应的参数配置
var RequestConfig = {
    0: 'RequestBody', 1: 'RequestParam'
}
//通用fetch方法
function fetchSelf(method, url, params, config) {
    return fetch(url, {
        credentials: 'include',
        method: method,
        headers: {
            'Content-Type': config ? 'application/json' : 'application/x-www-form-urlencoded'
        },
        body: params
    }).then(function (res) {
        if (res.status >= 200 && res.status < 300) {
            return res;
        } else {
            var error = new Error(res.statusText)
            error.response = res;
            throw error
        }
    }).then(function (res) {
        return res.json();
    }).catch(function (error) {
        notie.alert(3, error, 3);
        console.log('request failed', error)
    })
}

//通用load user方法
(function loadUseInfo() {
    //fetchSelf('POST', '/login.html', null).then(function (data) {
    //    if (data.uid) {
    //        $('[data-use="showUserInfo"]').html(data.displayName);
    //    } else {
    //        alert('未登录');
    //    }
    //
    //})
})();




