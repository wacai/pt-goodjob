/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : wac_goodjob

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 07/04/2016 16:54:30 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sched_dependency_check`
-- ----------------------------
DROP TABLE IF EXISTS `sched_dependency_check`;
CREATE TABLE `sched_dependency_check` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_exec_id` bigint(20) unsigned DEFAULT NULL COMMENT '主task execute id',
  `state` tinyint(4) DEFAULT NULL COMMENT '0:task结束, 1:task未结束',
  `jobs_map` varchar(1024) DEFAULT NULL COMMENT 'task中的所有任务',
  `job_ids_map` varchar(1024) DEFAULT NULL COMMENT '依赖任务id集合',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `lastUpdateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务依赖拆分表';

-- ----------------------------
--  Table structure for `sched_project`
-- ----------------------------
DROP TABLE IF EXISTS `sched_project`;
CREATE TABLE `sched_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL COMMENT '项目中文名称',
  `code` varchar(64) DEFAULT NULL COMMENT '项目code',
  `p_desc` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目表';

-- ----------------------------
--  Records of `sched_project`
-- ----------------------------
BEGIN;
INSERT INTO `sched_project` VALUES ('1', '测试项目A', 'testA_code1', null);
COMMIT;

-- ----------------------------
--  Table structure for `sched_sub_job`
-- ----------------------------
DROP TABLE IF EXISTS `sched_sub_job`;
CREATE TABLE `sched_sub_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '本次任务id',
  `task_exec_id` bigint(20) unsigned DEFAULT NULL COMMENT '主task execute id',
  `host_ip` varchar(32) DEFAULT NULL COMMENT '任务执行的任务机ip',
  `job_name` varchar(64) DEFAULT NULL COMMENT 'job的名字',
  `job_group` varchar(64) DEFAULT NULL COMMENT 'job对应的dubbo group',
  `job_param` varchar(256) DEFAULT NULL COMMENT 'job参数',
  `start_time` bigint(20) DEFAULT NULL COMMENT '开始执行时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '结束执行时间',
  `job_offset` bigint(20) DEFAULT NULL COMMENT '执行偏移量',
  `retry_count` tinyint(4) DEFAULT '0' COMMENT '重试次数',
  `retry_start_time` bigint(20) DEFAULT NULL COMMENT '重试开始时间',
  `state` tinyint(4) DEFAULT NULL COMMENT '0:成功, 1:失败, 2:处理中, 3:超时, 4:重试中, 5:准备好',
  `exec_msg` varchar(4096) DEFAULT NULL COMMENT '错误信息',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `lastUpdateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_job_task_exec_id` (`task_exec_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务详细信息';

-- ----------------------------
--  Table structure for `sched_task_config`
-- ----------------------------
DROP TABLE IF EXISTS `sched_task_config`;
CREATE TABLE `sched_task_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(32) DEFAULT NULL COMMENT '任务名称',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '0:删除,1:正常',
  `task_state` tinyint(4) DEFAULT NULL COMMENT '任务状态， 0:停用，1:启用',
  `task_desc` varchar(64) DEFAULT NULL COMMENT '任务描述',
  `task_group` varchar(256) DEFAULT NULL COMMENT '业务code，必须唯一，对应dubbo group',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `next_fire_time` bigint(20) DEFAULT NULL COMMENT '任务下次执行时间',
  `prev_fire_time` bigint(20) DEFAULT NULL COMMENT '任务上次执行时间',
  `retry` tinyint(4) DEFAULT '0' COMMENT '重试次数',
  `delay_skip` tinyint(4) DEFAULT '0' COMMENT '是否跳过， 0:不跳过， 1:跳过 ，具体见exact_once',
  `failure_skip` tinyint(4) DEFAULT NULL COMMENT '失败跳过',
  `call_type` tinyint(4) DEFAULT '0' COMMENT '调用方式 0:dubbo, 1:http',
  `next_skip` tinyint(4) DEFAULT NULL COMMENT '跳过下次执行',
  `exact_once` tinyint(4) DEFAULT '0' COMMENT '精确执行 0:是，1:否 --  0表示上次没执行完就不执行， 1表示上次没执行完，如果超过超时（timeout）时间还会执行',
  `timeout` int(11) DEFAULT NULL COMMENT '超时(单位秒)',
  `cron_exp` varchar(256) DEFAULT NULL COMMENT 'cron表达式',
  `return` tinyint(4) DEFAULT '0' COMMENT '0:需要返回结果， 1:不需要',
  `type` tinyint(3) unsigned DEFAULT '0' COMMENT '0:默认, 1:分片, 2:任务依赖',
  `job_data` varchar(1024) DEFAULT NULL COMMENT 'job 依赖关系，json格式',
  `lastUpdateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8 COMMENT='任务配置表';

-- ----------------------------
--  Records of `sched_task_config`
-- ----------------------------
BEGIN;
INSERT INTO `sched_task_config` VALUES ('1001', '测试任务', '1', '1', '测试A', 'goodjob-example_j6g1', '1', '1467615960000', '1467616020000', null, '1', null, '0', null, '1', '180', '0 0/1 * * * ? ', '0', '0', null, '2016-07-04 15:06:00', null, '玄武');
COMMIT;

-- ----------------------------
--  Table structure for `sched_task_execute`
-- ----------------------------
DROP TABLE IF EXISTS `sched_task_execute`;
CREATE TABLE `sched_task_execute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_config_id` int(11) DEFAULT NULL,
  `task_config_type` tinyint(4) unsigned DEFAULT NULL COMMENT '0:默认, 1:分片, 2:任务依赖',
  `job_data` varchar(1024) DEFAULT NULL COMMENT 'job 依赖关系，json格式',
  `exec_type` tinyint(4) unsigned DEFAULT NULL COMMENT '0:默认, 1:手动',
  `task_name` varchar(32) DEFAULT NULL COMMENT '任务名称',
  `exec_desc` varchar(32) DEFAULT NULL COMMENT '执行信息',
  `start_time` bigint(20) DEFAULT NULL COMMENT '任务开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '任务结束时间',
  `job_count` tinyint(4) DEFAULT '1' COMMENT 'job个数',
  `percent` varchar(16) DEFAULT NULL COMMENT '任务百分比',
  `state` tinyint(4) DEFAULT NULL COMMENT '执行状态   0:成功, 1:失败, 2:处理中, 3:超时',
  `exec_msg` varchar(512) DEFAULT NULL COMMENT '执行信息',
  `sched_time` bigint(20) DEFAULT NULL COMMENT '调度时间',
  `lastUpdateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_execute_task_config_id` (`task_config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务执行表';

-- ----------------------------
--  Table structure for `sched_task_host`
-- ----------------------------
DROP TABLE IF EXISTS `sched_task_host`;
CREATE TABLE `sched_task_host` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_config_id` int(11) DEFAULT NULL COMMENT '任务配置ID',
  `ip` varchar(32) DEFAULT NULL,
  `owner` tinyint(4) DEFAULT '0' COMMENT '0:否, 1:是',
  `disabled` tinyint(4) DEFAULT '1' COMMENT '0:不可用, 1:可用',
  `host_desc` varchar(64) DEFAULT NULL,
  `lastUpdateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务行机主机表';


-- ----------------------------
--  Table structure for `sched_task_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `sched_task_monitor`;
CREATE TABLE `sched_task_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `task_config_id` int(11) DEFAULT NULL COMMENT '任务配置ID',
  `mail_enable` tinyint(4) DEFAULT NULL COMMENT '0:不可用, 1:可能',
  `mobile_enble` tinyint(4) DEFAULT NULL COMMENT '0:不可用, 1:可能',
  `type` tinyint(4) DEFAULT NULL COMMENT '冗余',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控配置表';

-- ----------------------------
--  Table structure for `sched_task_param`
-- ----------------------------
DROP TABLE IF EXISTS `sched_task_param`;
CREATE TABLE `sched_task_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param` varchar(256) DEFAULT NULL COMMENT 'json格式',
  `task_config_id` int(11) DEFAULT NULL COMMENT '任务配置ID',
  `lastUpdateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分片参数表';

-- ----------------------------
--  Table structure for `sched_user`
-- ----------------------------
DROP TABLE IF EXISTS `sched_user`;
CREATE TABLE `sched_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code, 从bops获取',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobileTel` varchar(16) DEFAULT NULL COMMENT '手机',
  `lastUpdateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
