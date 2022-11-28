/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : em_demo

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 28/11/2022 11:47:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hero
-- ----------------------------
DROP TABLE IF EXISTS `hero`;
CREATE TABLE `hero`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `way_id` int NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hero
-- ----------------------------
INSERT INTO `hero` VALUES (1, 1, '菲欧娜');
INSERT INTO `hero` VALUES (2, 1, '盖伦');
INSERT INTO `hero` VALUES (3, 2, '李青');
INSERT INTO `hero` VALUES (4, 2, '格雷福斯');
INSERT INTO `hero` VALUES (5, 3, '瑞兹');
INSERT INTO `hero` VALUES (6, 3, '卡特琳娜');
INSERT INTO `hero` VALUES (7, 4, '艾希');
INSERT INTO `hero` VALUES (8, 4, '凯特琳');
INSERT INTO `hero` VALUES (9, 5, '索拉卡');
INSERT INTO `hero` VALUES (10, 5, '娑娜');

-- ----------------------------
-- Table structure for way
-- ----------------------------
DROP TABLE IF EXISTS `way`;
CREATE TABLE `way`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of way
-- ----------------------------
INSERT INTO `way` VALUES (1, '上路');
INSERT INTO `way` VALUES (2, '打野');
INSERT INTO `way` VALUES (3, '中路');
INSERT INTO `way` VALUES (4, '下路');
INSERT INTO `way` VALUES (5, '辅助');

SET FOREIGN_KEY_CHECKS = 1;
