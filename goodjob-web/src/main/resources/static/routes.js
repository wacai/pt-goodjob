/**
 * Created by baojun on 16/3/25.
 */
module.exports = {
    '/task': '/task/index.html',//task首页
    '/exec': '/exec/index.html',//运行首页
    
    
    'POST::/login.html':'mock::userInfo.js',//用户登录的信息

    'POST::/taskConfig/find_task_config.html': 'mock::task/tasklist.js',//获取调度列表
    'POST::/taskExecute/find_task_execute.html': 'mock::exec/execlist.js',//获取调度运行列表
    'POST::/project/add_project.html': 'mock::task/add_project.js',//新增项目

    'POST::/project/find_all_project.html': 'mock::task/all_projects.js',//新增项目

    'POST::/taskConfig/find_task_config_by_id.html': 'mock::task/findTaskByTaskId.js',//根据projectId获取task列表

    'POST::/taskConfig/find_task_config_drop.html': 'mock::task/findTaskByProjectId.js',//根据projectId获取task列表
    'POST::/taskConfig/add_task.html': 'mock::task/modify_add_task.js',//新增task
    'POST::/taskConfig/modify_task.html': 'mock::task/modify_add_task.js',//更新task

    'POST::/taskConfig/start_Task.html': 'mock::task/startTask.js',//启动任务
    'POST::/taskConfig/restart_task.html': 'mock::task/startTask.js',//重新启动任务
    
    'POST::/taskConfig/stop_task.html': 'mock::task/stopTask.js',//停止任务
    'POST::/taskConfig/trigger_task.html': 'mock::task/triggerTask.js',//触发任务
    'POST::/subJob/find_sub_job.html': 'mock::exec/subjoblist.js',//子任务

    'POST::/taskConfig/delete_task.html': 'mock::task/delTask.js',//删除task
    'POST::/taskConfig/update_cron.html': 'mock::task/updateCron.js',

    'POST::/taskParam/find_task_param.html': 'mock::taskParam/paramlist.js',
    'POST::/taskParam/add_param.html': '',//add
    'POST::/taskParam/delete_param.html': 'mock::taskParam/deleteParam.js',//delete
    'POST::/taskParam/modify_param.html': '',//update

    // 'ALL::/project/:pattern*': 'http://127.0.0.1:8086/project/',
    // 'ALL::/taskConfig/:pattern*': 'http://127.0.0.1:8086/taskConfig/',
    // 'ALL::/taskExecute/:pattern*': 'http://127.0.0.1:8086/taskExecute/',
    // 'ALL::/subJob/:pattern*': 'http://127.0.0.1:8086/subJob/',
    // 'ALL::/taskParam/:pattern*': 'http://127.0.0.1:8086/taskParam/',

};
