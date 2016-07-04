/**
 * Created by baojun on 16/4/5.
 */
$(function () {

    template.helper('setOperationsForTaskList', function (taskState) {

        return renderTaskListOptions(taskState);
    });

    function renderTaskListOptions(taskState) {
        var html = renderContentByTpl('tasklist_options_tpl', {'task_state': taskState});
        return html;
    }

    initProjectCond();

    function initProjectCond() {
        fetchSelf('POST', '/project/find_all_project.html', null).then(function (data) {
            var html = renderContentByTpl('projectId_options', {plist: data});
            $('#projectId').empty().html(html)
        })
    }


    function showTaskParam($target) {
        var configId = $target.data('taskconfigid');
        readTemplate('./tpl_task_param.html').done(function (render) {
            fetchSelf('POST', '/taskParam/find_task_param.html', 'taskConfigId=' + configId).then(function (data) {
                var renderData = {plist: data, taskName: $target.data('taskname')};
                var html = render(renderData);
                $('#paramModal .modal-body').html(html);
                $('#paramModal').modal('show');
            })
        })
    }

    function showHosts($target) {
        var configId = $target.data('taskconfigid');
        readTemplate('./tpl_host_grid.html').done(function (render) {
            fetchSelf('POST', '/taskHost/find_task_host.html', 'taskConfigId=' + configId).then(function (data) {
                var renderData = {taskConfigId: configId, hostList: data, taskName: $target.data('taskname')};
                var html = render(renderData);
                $('#hostModal .modal-body').empty().html(html);
                $('#hostModal').modal('show');
            })
        })
    }

    function refreshHosts(taskId, taskname) {
        readTemplate('./tpl_host_grid.html').done(function (render) {
            fetchSelf('POST', '/taskHost/find_task_host.html', 'taskConfigId=' + taskId).then(function (data) {
                var renderData = {taskConfigId: taskId, hostList: data, taskName: taskname};
                var html = render(renderData);
                $('#hostModal .modal-body').empty().html(html);
            })
        })
    }

    /**
     * 调用弹窗显示依赖树
     * @param $target
     */
    function showDepTree($target) {
        var execId = $target.data('taskconfigid');
        readTemplate('../exec/tpl_subjob_tree.html').done(function (render) {
            var renderData = {'subJobList': {}};
            var html = render(renderData);
            $('#depModal .modal-body').html(html);
            $('#depModal').modal('show');
        })
    }

    $(document).on('change', function (ev) {
        var $target = $(ev.target);
        var tid = $target.attr('id');
        switch (tid) {
            case 'sel_projectId':
                var from = $('#addModal').find('[data-use="do_put_task"]').data('from');
                if (from == 'add') {
                    var $codeOption = $target.find('option:selected');
                    if ($target.val() !== '-1') {
                        var code = $codeOption.data('projectcode');
                        $('#taskGroupCode').val(code + '_');
                    } else {
                        $('#taskGroupCode').val('');
                    }
                }

                break;
            case 'projectId':
                queryTaskList();
                break;
            case 'changeTaskType':
                var type = $target.val();
                if (type != 0) {
                    $('[data-relation="changeTaskType"][data-val=' + type + ']').show();
                    $('[data-relation="changeTaskType"][data-val!=' + type + ']').hide();
                } else {
                    $('[data-relation="changeTaskType"]').hide();
                }
                break;
            case 'delay_skip_control':
                var val = $target.val();
                if (val == 0) {
                    $('[data-relation="delay_skip_control"]').addClass('hidden').attr('disabled', 'disabled');
                    $('#exact_once_info').addClass('hidden');

                } else {
                    $('[data-relation="delay_skip_control"]').removeClass('hidden').removeAttr('disabled');
                    $('#exact_once_info').removeClass('hidden');
                }

                break;
            default:
                break;
        }

    });


    //获得列表中被选中的任务
    function getSelectedTasks() {
        var $box = $('#task_grid'), $select_task = $box.find('tbody [id^="task_checkbox_"]:checked');
        var selectedTaskIds = [];
        for (var i = 0; i < $select_task.length; i++) {
            selectedTaskIds.push($($select_task[i]).data('taskconfigid'))
        }
        return selectedTaskIds;
    }

    //获得列表中被选中的一个任务
    function getSelectedOneTasks() {
        var $box = $('#task_grid'), $select_task = $box.find('tbody [id^="task_checkbox_"]:checked');
        var id = null;
        if($select_task.length == 1){
            id = $($select_task[0]).data('taskconfigid');
        }

        return id;
    }

    $(document).on('click', function (ev) {
        var target = ev.target, $target = $(target);
        var workItem = $target.attr('data-use');

        switch (workItem) {
            case 'project-add':
                var d = dialog({
                    id: 'add-project-dialog',
                    title: '添加项目',
                    align: 'right',
                    content: document.getElementById('project_add_form'),
                    ok: function () {
                        var that = this;
                        this.title('正在提交..');
                        fetchSelf('POST', '/project/add_project.html', JSON.stringify($('#project_add_form').serializeObject()), 1).then(function (data) {
                            if (data.code == "0") {
                                notie.alert(1, data.msg, 2);
                                that.close().remove();
                                initProjectCond();
                                $('#project_add_form').clearForm();

                            } else {
                                notie.alert(3, data.msg, 2);
                            }
                        });
                        return false;
                    },
                    okValue: '添加项目',
                    cancel: false,
                    cancelValue: '取消',
                    quickClose: true// 点击空白处快速关闭
                });
                d.show(document.getElementById('btn_project_add'));

                break;

            case 'ip-add':
                var d = dialog({
                    id: 'add-ip-dialog',
                    zIndex: 9999,
                    title: '添加ip',
                    align: 'right',
                    content: document.getElementById('ip_add_form'),
                    ok: function () {
                        var that = this;
                        this.title('正在提交..');
                        var hostModel = new Object();
                        hostModel.ip = $('#ip').val();
                        hostModel.task_config_id = $('#hostTaskId').val();
                        fetchSelf('POST', '/taskHost/add_host.html', JSON.stringify(hostModel), 1).then(function (data) {
                            if (data.code == "0") {
                                notie.alert(1, data.msg, 2);
                                that.close().remove();
                                initProjectCond();
                                $('#ip_add_form').clearForm();

                            } else {
                                notie.alert(3, data.msg, 2);
                            }
                        });
                        return false;
                    },
                    okValue: '添加IP',
                    cancel: false,
                    cancelValue: '取消',
                    quickClose: true// 点击空白处快速关闭
                });
                d.show(document.getElementById('btn_ip_add'));

                break;

            case 'select_task':
                var $box = $('#task_grid');
                var taskId = $target.data('taskconfigid');
                if (taskId === undefined) {
                    $box.find('tbody [id^="task_checkbox_"]').prop('checked', $target.prop('checked'));

                }
                var $select_task = $box.find('tbody [id^="task_checkbox_"]:checked'), len = $select_task.length;

                if (len) {
                    if (len > 1) {
                        $('[data-use="task-del"],[data-use="task-update"]').attr('disabled', 'disabled');
                    } else {
                        $('[data-use="task-del"],[data-use="task-update"]').removeAttr('disabled');
                    }

                } else {
                    $('[data-use="task-del"],[data-use="task-update"]').attr('disabled', 'disabled');
                }

                break;
            case 'add_param_control':
                var $box = $('[data-relation="changeTaskType"][data-val="1"]'), index = $box.data('total');
                var html = renderContentByTpl('add_param_control_tpl');
                $box.append(html).data('total', index + 1);
                $('#add_param_count').text(index + 1);
                break;
            case 'del_param_control':
                var paramId = $target.data('paramid');
                if (paramId) {
                    notie.confirm('删除分片?', 'OK', 'Cancel', function () {

                        fetchSelf('POST', '/taskParam/delete_param.html', 'paramId=' + paramId).then(function (data) {
                            if (data.code == '0') {
                                var $box = $('[data-relation="changeTaskType"][data-val="1"]'), index = $box.data('total');
                                $target.closest('.col-sm-4').remove();
                                $box.data('total', index - 1);
                                $('#add_param_count').text(index - 1);
                                notie.alert(1, '删除成功', 1);
                            } else {
                                notie.alert(3, '删除失败', 1);
                            }
                        })
                    })

                } else {
                    var $box = $('[data-relation="changeTaskType"][data-val="1"]'), index = $box.data('total');
                    $target.closest('.col-sm-4').remove();
                    $box.data('total', index - 1);
                    $('#add_param_count').text(index - 1);
                }

                break;
            case 'query_tasklist':
                queryTaskList();
                break;
            case 'ip-detect':
                detectHosts();
                break;
            case 'show_taskparam':
                showTaskParam($target);
                break;
            case 'show_hosts':
                showHosts($target);
                break;
            case 'show_subjob':
                showDepTree($target);
                break;

            case 'task-del':

                var from = $target.data('from');
                if (from === 'toolbar') {
                    var selectedTask = getSelectedOneTasks();
                    if(selectedTask != null){
                        notie.confirm('确定删除任务', 'OK', 'Cancel', function () {
                            del_task(selectedTask);
                        });
                    }
                } else {
                    var taskName = $target.closest('td').data('taskname'), taskid = $target.closest('td').data('taskconfigid');
                    notie.confirm('确定删除任务【' + taskName + '】', 'OK', 'Cancel', function () {
                        del_task(taskid, $target);
                    });
                }

                break;
            case 'task-start':
                var taskName = $target.closest('td').data('taskname'), taskid = $target.closest('td').data('taskconfigid');
                var state = $target.closest('td').data('taskstate');
                if (state == '0') {
                    notie.confirm('启用任务【' + taskName + '】', 'OK', 'Cancel', function () {
                        startTask(taskid, $target);
                    });
                }

                break;
            case 'task-restart':
                var taskName = $target.closest('td').data('taskname'), taskid = $target.closest('td').data('taskconfigid');
                var state = $target.closest('td').data('taskstate');
                if (state == '1') {
                    notie.confirm('重启任务【' + taskName + '】', 'OK', 'Cancel', function () {
                        reStartTask(taskid, $target);
                    });
                }
                break;
            case 'task-stop':
                var taskName = $target.closest('td').data('taskname'), taskid = $target.closest('td').data('taskconfigid');
                var state = $target.closest('td').data('taskstate');
                if (state == '1') {
                    notie.confirm('停用任务【' + taskName + '】', 'OK', 'Cancel', function () {
                        stopTask(taskid, $target);
                    });
                }

                break;
            case 'host-owner-yes':
                var hostId = $target.closest('td').data('id'), hostIp = $target.closest('td').data('ip');
                var taskId = $('#hostTaskId').val();
                var taskName = $('#hostTaskName').val();
                notie.confirm('指定【' + hostIp + '】为执行机', 'OK', 'Cancel', function () {
                    hostOwnerYes(hostId, hostIp, taskId, taskName);
                });


                break;
            case 'host-owner-no':
                var hostId = $target.closest('td').data('id'), hostIp = $target.closest('td').data('ip');
                var taskId = $('#hostTaskId').val();
                var taskName = $('#hostTaskName').val();
                notie.confirm('取消指定执行机【' + hostIp + '】', 'OK', 'Cancel', function () {
                    hostOwnerNo(hostId, hostIp, taskId, taskName);
                });

                break;
            case 'host-enable-yes':
                var hostId = $target.closest('td').data('id'), hostIp = $target.closest('td').data('ip');
                var taskId = $('#hostTaskId').val();
                var taskName = $('#hostTaskName').val();
                notie.confirm('启用执行机【' + hostIp + '】', 'OK', 'Cancel', function () {
                    hostEnableYes(hostId, hostIp, taskId, taskName);
                });

                break;
            case 'host-enable-no':
                var hostId = $target.closest('td').data('id'), hostIp = $target.closest('td').data('ip');
                var taskId = $('#hostTaskId').val();
                var taskName = $('#hostTaskName').val();
                notie.confirm('禁用执行机【' + hostIp + '】', 'OK', 'Cancel', function () {
                    hostEnableNo(hostId, hostIp, taskId, taskName);
                });

                break;
            case 'trigger-task':
                var taskName = $target.closest('td').data('taskname'), taskid = $target.closest('td').data('taskconfigid');
                var triggered = $target.data('tasktriggered');//执行状态

                notie.confirm('手动执行任务【' + taskName + '】', 'OK', 'Cancel', function () {
                    triggerTask(taskid, $target);
                });

                break;
            case 'task-update':
                readTemplate('./tpl_task_add.html').done(function (render) {
                    var taskId = getSelectedTasks()[0];
                    var renderObj = {};
                    fetchSelf('POST', '/taskConfig/find_task_config_by_id.html', 'id=' + taskId).then(function (data) {
                        console.log(data);
                        renderObj = data ? data : {};
                        return fetchSelf('POST', '/project/find_all_project.html', null)
                    }).then(function (data) {
                        renderObj.plist = data;
                        return fetchSelf('POST', '/taskParam/find_task_param.html', 'taskConfigId=' + taskId)
                    }).then(function (data) {
                        renderObj.paramList = data;
                        var html = render(renderObj);
                        $('#addModal .modal-body').html(html);
                        $('#addModal').modal('show');
                        $('#span_how_use').text('修改');
                        $('#addModal [data-use="do_put_task_and_restart"]').removeClass('hidden');

                        $('#addModal').find('[data-use="do_put_task"]').data('from', 'update');
                    });
                });
                break;
            case 'task-add':
                readTemplate('./tpl_task_add.html').done(function (render) {

                    fetchSelf('POST', '/project/find_all_project.html', null).then(function (data) {
                        var html = render({'plist': data});
                        $('#addModal .modal-body').html(html);
                        $('#addModal').modal('show');
                        $('#span_how_use').text('新增');
                        $('#addModal [data-use="do_put_task_and_restart"]').addClass('hidden');
                        $('#addModal').find('[data-use="do_put_task"]').data('from', 'add');
                    })
                });
                break;
            case 'do_put_task_and_restart':
                notie.confirm('保存之后会触发任务启用,确定保存并启用?', '确定', '取消', function () {

                    fetchSelf('POST', '/taskConfig/ modify_and_effect_task.html', JSON.stringify($('#put_task_form').serializeObject()), 1).then(function (data) {
                        var taskId = $('#put_task_form').find('[name="id"]').val();
                        if (data.code == "0") {
                            notie.alert(1, data.msg, 2);
                            $('#addModal').modal('hide');

                            readTemplate('./tpl_task_grid.html').done(function (render) {
                                fetchSelf('POST', '/taskConfig/find_task_config_by_id.html', 'id=' + taskId).then(function (data) {
                                    var arr = [];
                                    arr.push(data);
                                    var renderData = {'tasklist': arr};
                                    var html = render(renderData);
                                    $('tr[data-taskconfigid="' + taskId + '"]').replaceWith(html);
                                })
                            });

                        } else {
                            notie.alert(3, data.msg, 2);
                        }

                    });

                })

                break;
            case 'do_put_task':
                var from = $target.data('from');
                if (from === 'add') {

                    fetchSelf('POST', '/taskConfig/add_task.html', JSON.stringify($('#put_task_form').serializeObject()), 1).then(function (data) {
                        if (data.code == "0") {
                            notie.alert(1, data.msg, 2);
                            $('#addModal').modal('hide');
                            queryTaskList();

                        } else {
                            notie.alert(3, data.msg, 2);
                        }

                    });
                } else {
                    fetchSelf('POST', '/taskConfig/modify_task.html', JSON.stringify($('#put_task_form').serializeObject()), 1).then(function (data) {
                        var taskId = $('#put_task_form').find('[name="id"]').val();
                        if (data.code == "0") {
                            notie.alert(1, data.msg, 2);
                            $('#addModal').modal('hide');

                            readTemplate('./tpl_task_grid.html').done(function (render) {
                                fetchSelf('POST', '/taskConfig/find_task_config_by_id.html', 'id=' + taskId).then(function (data) {
                                    var arr = [];
                                    arr.push(data);
                                    var renderData = {'tasklist': arr};
                                    var html = render(renderData);
                                    $('tr[data-taskconfigid="' + taskId + '"]').replaceWith(html);
                                })
                            });

                        } else {
                            notie.alert(3, data.msg, 2);
                        }

                    });
                }
                break;

            default:
                break;
        }

    });


    function hostOwnerYes(hostId, hostIp, taskId, taskName){
        var hostHandle = new Object();
        hostHandle.id = hostId;
        hostHandle.taskId = taskId;
        fetchSelf('POST', '/taskHost/assign_host_owner.html', JSON.stringify(hostHandle), 1).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            refreshHosts(taskId, taskName);
        })
    }

    function hostOwnerNo(hostId, hostIp, taskId, taskName){
        var hostHandle = new Object();
        hostHandle.id = hostId;
        hostHandle.taskId = taskId;
        fetchSelf('POST', '/taskHost/cancel_host_owner.html', JSON.stringify(hostHandle), 1).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            refreshHosts(taskId, taskName);
        })
    }

    function hostEnableYes(hostId, hostIp, taskId, taskName){
        var hostHandle = new Object();
        hostHandle.id = hostId;
        hostHandle.taskId = taskId;
        fetchSelf('POST', '/taskHost/start_host.html', JSON.stringify(hostHandle), 1).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            refreshHosts(taskId, taskName);
        })
    }

    function hostEnableNo(hostId, hostIp, taskId, taskName){
        var hostHandle = new Object();
        hostHandle.id = hostId;
        hostHandle.taskId = taskId;
        fetchSelf('POST', '/taskHost/stop_host.html', JSON.stringify(hostHandle), 1).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            refreshHosts(taskId, taskName);
        })
    }

    function detectHosts(){
        var taskId = $('#hostTaskId').val();
        var taskName = $('#hostTaskName').val();
        fetchSelf('POST', '/taskHost/detect_hosts.html', 'taskConfigId=' + taskId).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            refreshHosts(taskId, taskName);
        })
    }

    function del_task(task, $target) {
        if (task instanceof Array) {
            console.log('TODO 批量删除');
        } else {
            fetchSelf('POST', '/taskConfig/delete_task.html', 'taskId=' + task).then(function (data) {
                notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
                queryTaskList();
            })
        }
        queryTaskList();
    }

    function startTask(taskid, $target) {
        fetchSelf('POST', '/taskConfig/start_Task.html', 'taskId=' + taskid).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            if (data.code == '0') {
                $target.closest('td').data('task_state', '1');
                $target.closest('tr').find('[data-relation="task_state"]').text('启用');

                var html = renderTaskListOptions(1);
                $target.closest('td').data('taskstate', 1).empty().html(html);
            }

            queryTaskList();
        })
    }

    function reStartTask(taskid, $target) {
        fetchSelf('POST', '/taskConfig/restart_task.html', 'taskId=' + taskid).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            if (data.code == '0') {
                $target.closest('td').data('task_state', '1');
                // $target.closest('tr').find('[data-relation="task_state"]').text('启用');

                var html = renderTaskListOptions(1);
                $target.closest('td').data('taskstate', 1).empty().html(html);
            }

            queryTaskList();
        })

    }

    function triggerTask(taskid, $target) {
        fetchSelf('POST', '/taskConfig/trigger_task.html', 'taskId=' + taskid).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            if (data.code == '0') {
                // $target.removeClass('glyphicon-play-circle').addClass('glyphicon-dashboard');
                $target.data('tasktriggered', 1);
            }
        })

    }

    function stopTask(taskid, $target) {
        fetchSelf('POST', '/taskConfig/stop_task.html', 'taskId=' + taskid).then(function (data) {
            notie.alert(data.code == '0' ? 1 : 3, data.msg, 2);
            if (data.code == '0') {
                $target.closest('td').data('task_state', '0');
                $target.closest('tr').find('[data-relation="task_state"]').text('停用');
                var html = renderTaskListOptions(0);
                $target.closest('td').data('taskstate', 0).empty().html(html);

            }
            queryTaskList();
        })

    }


    function queryTaskList() {
        $('[data-use="task-del"],[data-use="task-update"]').attr('disabled', 'disabled');
        readTemplate('./tpl_task_grid.html').done(function (render) {
            fetchSelf('POST', '/taskConfig/find_task_config.html', JSON.stringify($('#query_task_form').serializeObject()), 1).then(function (data) {
                var renderData = {'tasklist': data};
                var html = render(renderData);
                $('#task_grid tbody').empty().append(html);
                //固定toolbar
                $('#grid_option_group').scrollspy({
                    min: $('#grid_option_group').offset().top - 50,
                    max: $('.main').height(),
                    onEnter: function (element, position) {
                        $('#grid_option_group_box').show();

                    },
                    onLeave: function (element, position) {
                        $('#grid_option_group_box').hide();
                    }
                });
            })
        });
    }
})
