/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50640
 Source Host           : localhost:3306
 Source Schema         : xc_course

 Target Server Type    : MySQL
 Target Server Version : 50640
 File Encoding         : 65001

 Date: 28/10/2019 20:40:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
    `name`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
    `label`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类标签默认和名称一样',
    `parentid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父结点id',
    `isshow`   char(1) CHARACTER SET utf8 COLLATE utf8_general_ci     NULL DEFAULT NULL COMMENT '是否显示',
    `orderby`  int(4)                                                 NULL DEFAULT NULL COMMENT '排序字段',
    `isleaf`   char(1) CHARACTER SET utf8 COLLATE utf8_general_ci     NULL DEFAULT NULL COMMENT '是否叶子',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_base
-- ----------------------------
DROP TABLE IF EXISTS `course_base`;
CREATE TABLE `course_base`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `name`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程名称',
    `users`       varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适用人群',
    `mt`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程大分类',
    `grade`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程等级',
    `studymodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '学习模式',
    `teachmode`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '授课模式',
    `description` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '课程介绍',
    `st`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程小分类',
    `status`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '课程状态',
    `company_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '教育机构',
    `user_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '创建用户',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_market
-- ----------------------------
DROP TABLE IF EXISTS `course_market`;
CREATE TABLE `course_market`
(
    `id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
    `charge`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收费规则，对应数据字典',
    `valid`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '有效性，对应数据字典',
    `expires`    timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '过期时间',
    `qq`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '咨询qq',
    `price`      float(10, 2)                                           NULL     DEFAULT NULL COMMENT '价格',
    `price_old`  float(10, 2)                                           NULL     DEFAULT NULL COMMENT '原价',
    `start_time` datetime(0)                                            NULL     DEFAULT NULL COMMENT '课程有效期-开始时间',
    `end_time`   datetime(0)                                            NULL     DEFAULT NULL COMMENT '课程有效期-结束时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_off
-- ----------------------------
DROP TABLE IF EXISTS `course_off`;
CREATE TABLE `course_off`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键',
    `name`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程名称',
    `users`       varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '适用人群',
    `mt`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '大分类',
    `st`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '小分类',
    `grade`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程等级',
    `studymodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '学习模式',
    `description` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '课程介绍',
    `timestamp`   timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    `charge`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '收费规则，对应数据字典',
    `valid`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '有效性，对应数据字典',
    `qq`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '咨询qq',
    `price`       float(10, 2)                                            NOT NULL COMMENT '价格',
    `price_old`   float(10, 2)                                            NULL     DEFAULT NULL COMMENT '原价格',
    `expires`     timestamp(0)                                            NULL     DEFAULT NULL COMMENT '过期时间',
    `pic`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '课程图片',
    `teachplan`   text CHARACTER SET utf8 COLLATE utf8_general_ci         NOT NULL COMMENT '课程计划',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_pic
-- ----------------------------
DROP TABLE IF EXISTS `course_pic`;
CREATE TABLE `course_pic`
(
    `courseid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程id',
    `pic`      varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片id',
    PRIMARY KEY (`courseid`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_pre
-- ----------------------------
DROP TABLE IF EXISTS `course_pre`;
CREATE TABLE `course_pre`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键',
    `name`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程名称',
    `users`       varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '适用人群',
    `mt`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '大分类',
    `st`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '小分类',
    `grade`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程等级',
    `studymodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '学习模式',
    `description` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '课程介绍',
    `status`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程状态',
    `timestamp`   timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    `charge`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '收费规则，对应数据字典',
    `valid`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '有效性，对应数据字典',
    `qq`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '咨询qq',
    `price`       float(10, 2)                                            NOT NULL COMMENT '价格',
    `price_old`   float(10, 2)                                            NULL     DEFAULT NULL COMMENT '原价格',
    `expires`     timestamp(0)                                            NULL     DEFAULT NULL COMMENT '过期时间',
    `pic`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '课程图片',
    `teachplan`   text CHARACTER SET utf8 COLLATE utf8_general_ci         NOT NULL COMMENT '课程计划',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course_pub
-- ----------------------------
DROP TABLE IF EXISTS `course_pub`;
CREATE TABLE `course_pub`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键',
    `name`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程名称',
    `users`       varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '适用人群',
    `mt`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '大分类',
    `st`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '小分类',
    `grade`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程等级',
    `studymodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '学习模式',
    `teachmode`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '教育模式',
    `description` text CHARACTER SET utf8 COLLATE utf8_general_ci         NOT NULL COMMENT '课程介绍',
    `timestamp`   timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳logstash使用',
    `charge`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '收费规则，对应数据字典',
    `valid`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '有效性，对应数据字典',
    `qq`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '咨询qq',
    `price`       float(10, 2)                                            NULL     DEFAULT NULL COMMENT '价格',
    `price_old`   float(10, 2)                                            NULL     DEFAULT NULL COMMENT '原价格',
    `expires`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '过期时间',
    `start_time`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '课程有效期-开始时间',
    `end_time`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '课程有效期-结束时间',
    `pic`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '课程图片',
    `teachplan`   text CHARACTER SET utf8 COLLATE utf8_general_ci         NOT NULL COMMENT '课程计划',
    `pub_time`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '发布时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for teachplan
-- ----------------------------
DROP TABLE IF EXISTS `teachplan`;
CREATE TABLE `teachplan`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `pname`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `parentid`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `grade`       char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '层级，分为1、2、3级',
    `ptype`       char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NULL DEFAULT NULL COMMENT '课程类型:1视频、2文档',
    `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '章节及课程时介绍',
    `timelength`  double(5, 2)                                            NULL DEFAULT NULL COMMENT '时长，单位分钟',
    `courseid`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '课程id',
    `orderby`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '排序字段',
    `status`      char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：未发布、已发布',
    `trylearn`    char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NULL DEFAULT NULL COMMENT '是否试学',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for teachplan_media
-- ----------------------------
DROP TABLE IF EXISTS `teachplan_media`;
CREATE TABLE `teachplan_media`
(
    `teachplan_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程计划id',
    `media_id`               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '媒资文件id',
    `media_fileoriginalname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '媒资文件的原始名称',
    `media_url`              varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '媒资文件访问地址',
    `courseid`               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程Id',
    PRIMARY KEY (`teachplan_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for teachplan_media_pub
-- ----------------------------
DROP TABLE IF EXISTS `teachplan_media_pub`;
CREATE TABLE `teachplan_media_pub`
(
    `teachplan_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程计划id',
    `media_id`               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '媒资文件id',
    `media_fileoriginalname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '媒资文件的原始名称',
    `media_url`              varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '媒资文件访问地址',
    `courseid`               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '课程Id',
    `timestamp`              timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT 'logstash使用',
    PRIMARY KEY (`teachplan_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
