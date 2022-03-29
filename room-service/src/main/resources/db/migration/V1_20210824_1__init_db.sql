-- ----------------------------
-- Table structure for rm_appointment
-- ----------------------------
DROP TABLE IF EXISTS `rm_appointment`;
CREATE TABLE `rm_appointment`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `event_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `timestamp` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `appointment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `patient_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `patient_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `provider_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `provider_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `start_timestamp` timestamp(0) NULL DEFAULT NULL,
  `end_timestamp` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `remaining_seconds` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `user_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `provider_device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `patient_device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `remaining_meeting_seconds` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `appointment_info_state` tinyint(2) UNSIGNED NULL DEFAULT 0 COMMENT '0: \"unknown\",\r\n1: \"appointment_info_push\",\r\n2: \"appointment_info_ring\"',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_appointment_appointmentid`(`appointment_id`) USING BTREE,
  INDEX `idx_appointment_eventid`(`event_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_extra
-- ----------------------------
DROP TABLE IF EXISTS `rm_extra`;
CREATE TABLE `rm_extra`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `broadcast_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `user_fullName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `room_owner` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_extra_roomid`(`room_id`) USING BTREE,
  INDEX `idx_extra_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_p2p_user
-- ----------------------------
DROP TABLE IF EXISTS `rm_p2p_user`;
CREATE TABLE `rm_p2p_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `broadcast_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `last_relay_server_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `last_relay_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `receiving_from` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `broadcast_initiator` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `relay_server` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `can_relay` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `max_relay_count_per_user` int(5) UNSIGNED NULL DEFAULT 0,
  `session_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `socket` blob NULL,
  `relay_receiver` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `video_source_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: \"unknown\",\r\n1: \"zoom video\",\r\n2: \"webrtc video\"',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_p2p_user_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_participant
-- ----------------------------
DROP TABLE IF EXISTS `rm_participant`;
CREATE TABLE `rm_participant`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_table_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT 'sys_room.id',
  `participant` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_participant_roomid`(`room_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_patient
-- ----------------------------
DROP TABLE IF EXISTS `rm_patient`;
CREATE TABLE `rm_patient`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `patient_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `event_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `event_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `timestamp` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `appointment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `nurse_fullname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `body_temperature` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `pulse_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `respiration_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `blood_pressure_high` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `blood_pressure_low` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `pulse_oximetry` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `recent_weight` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `pain_scale_assessment` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `created` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `provider_device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_patient_appointmentid`(`appointment_id`) USING BTREE,
  INDEX `idx_patient_patientid`(`patient_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_room
-- ----------------------------
DROP TABLE IF EXISTS `rm_room`;
CREATE TABLE `rm_room`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `max_participants_Allowed` int(10) NULL DEFAULT NULL,
  `owner` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `identifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `participant` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `video_source_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: \"unknown\",\r\n1: \"zoom video\",\r\n2: \"webrtc video\"',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_room_roomid`(`room_id`) USING BTREE,
  INDEX `idx_room_participant`(`participant`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_type_of_streams
-- ----------------------------
DROP TABLE IF EXISTS `rm_type_of_streams`;
CREATE TABLE `rm_type_of_streams`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `offer_to_receive_audio` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `offer_to_receive_video` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `audio` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `video` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `screen` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `oneway` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `broadcast` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `scalable` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_streams_roomid`(`room_id`) USING BTREE,
  INDEX `idx_streams_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_user
-- ----------------------------
DROP TABLE IF EXISTS `rm_user`;
CREATE TABLE `rm_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `socket_message_Event` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `socket_custom_event` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `session_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `socket` blob NULL,
  `connected_with` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `video_source_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: \"unknown\",\r\n1: \"zoom video\",\r\n2: \"webrtc video\"',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = 'rm - indicates room' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_zoom_room
-- ----------------------------
DROP TABLE IF EXISTS `rm_zoom_room`;
CREATE TABLE `rm_zoom_room`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `max_size` int(10) NULL DEFAULT NULL,
  `current_size` int(10) NULL DEFAULT NULL,
  `video_source_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: \"unknown\",\r\n1: \"zoom video\",\r\n2: \"webrtc video\"',
  `appointment_session_Id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `appointment_status` tinyint(2) UNSIGNED NULL DEFAULT NULL COMMENT '0: \"unknown\",\r\n1: \"start\",\r\n2: \"leave\",\r\n3: \"zoom_exception_leave\",\r\n4: \"network_exception_leave\"',
  `appointment_start_time` timestamp(0) NULL DEFAULT NULL,
  `appointment_end_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `appointment_remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_zoom_roomid`(`room_id`) USING BTREE,
  INDEX `idx_zoom_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rm_zoom_user
-- ----------------------------
DROP TABLE IF EXISTS `rm_zoom_user`;
CREATE TABLE `rm_zoom_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `room_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `online` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `sent` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `session_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  `video_source_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: unknown\r\n1: zoom video\r\n2: webrtc video',
  `gmt_create` timestamp(0) NULL DEFAULT NULL,
  `gmt_modified` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '0: 未删除,\r\n1: 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_zoom_userid`(`user_id`) USING BTREE,
  INDEX `idx_zoom_roomid`(`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
