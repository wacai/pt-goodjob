/**
 * Created by baojun on 16/4/5.
 */
$(function () {
    $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii'});

    initQueryCond();

    function initQueryCond() {

        fetchSelf('POST', '/project/find_all_project.html', null).then(function (data) {
            var html = renderContentByTpl('projectId_options', {plist: data});
            $('#projectId').append(html);
        })
    }


    $('#projectId').on('change', function () {
        var projectid = $(this).val();
        if (projectid == '-1') {
            $('#taskConfigId').empty().attr('disabled', 'disabled');
        } else {
            fetchSelf('POST', '/taskConfig/find_task_config_drop.html', 'projectId=' + projectid).then(function (data) {
                var html = renderContentByTpl('taskConfigId_options', {'tlist': data});
                $('#taskConfigId').html(html).removeAttr('disabled');
                $('[data-use="query_execlist"]').popover('hide');
            })
        }
    });

    //分片
    function showTaskParam($target) {
        var configId = $target.data('taskconfigid');
        readTemplate('../task/tpl_task_param.html').done(function (render) {

            fetchSelf('POST', '/taskParam/find_task_param.html', 'taskConfigId='+ configId).then(function (data) {
                var renderData = {plist: data, taskName: $target.data('taskname')};
                var html = render(renderData);
                $('#paramModal .modal-body').html(html);
                $('#paramModal').modal('show');

            })

        })
    }

    /**
     * 调用弹窗显示依赖树
     * @param $target
     */
    function showDepTree($target) {
        var execId = $target.data('execid');
        readTemplate('./tpl_subjob_tree.html').done(function (render) {

            fetchSelf('POST', '/subJob/find_sub_job.html', 'taskExecId='+execId).then(function (data) {
                var renderData = {'subJobList': data,'taskName':$target.text()};
                var html = render(renderData);
                $('#depModal .modal-body').html(html);
                $('#depModal').modal('show');

            })
        })
    }


    function queryExecList() {
        readTemplate('./tpl_task_exec_grid.html').done(function (render) {

            fetchSelf('POST', '/taskExecute/find_task_execute.html', JSON.stringify($('#query_exec_form').serializeObject()),1).then(function (data) {
                var renderData = {'execlist': data};
                var html = render(renderData);
                $('#exec_grid tbody').empty().append(html);
            })
        });
    }

    $('[data-use="query_execlist"]').popover({
        placement: 'left',
        trigger: 'manual',
        content: "请选择task",
        animation: false
    })

    $(document).on('click', function (ev) {
        var target = ev.target, $target = $(target);
        var workItem = $target.attr('data-use');

        switch (workItem) {
            case 'show_job_param_msg':
                $target.popover({
                    placement: 'top',
                    trigger: 'hover',
                    content: $target.text(),
                    animation: false
                }).popover('show');
                break;
            case 'show_exec_msg':
                $target.popover({
                    placement: 'top',
                    trigger: 'hover',
                    content: $target.text(),
                    animation: false
                }).popover('show');

                break;
            case 'show_taskparam':
                showTaskParam($target);
                break;
            case 'show_subjob':
                showDepTree($target);
                break;
            case 'query_execlist':
                var taskConfigId = $('#taskConfigId').val();
                if (taskConfigId === null) {
                    $target.popover('show');
                    return;
                }
                queryExecList();
                break;
            default:
                break;
        }

    });
})
