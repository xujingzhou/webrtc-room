//package com.dten.healthcare.room.bean;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
///**
// * @Creator: Johnny Xu
// * @Date: 2021/7/26
// * @Description:
// */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@TableName(value = "rm_extra")
//public class ExtraDO {
//    private static final long serialVersionUID = 1026786193980349900L;
//
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @TableField("room_id")
//    private String roomId;
//
//    @TableField("user_id")
//    private String userId;
//
//    @TableField(value = "user_fullName")
//    private String userFullName;
//
//    @TableField(value = "broadcast_id")
//    private String broadcastId;
//
//    @TableField(value = "room_owner")
//    private boolean roomOwner;
//
//    public ExtraDO(String roomId, String userId, ExtraDTO extraDTO) {
//        this.roomId = roomId;
//        this.userId = userId;
//        this.userFullName = extraDTO.getUserFullName();
//        this.broadcastId = extraDTO.getBroadcastId();
//        this.roomOwner = extraDTO.isRoomOwner();
//    }
//
//    public ExtraDO(String roomId, ExtraDTO extraDTO) {
//        this.roomId = roomId;
//        this.userFullName = extraDTO.getUserFullName();
//        this.broadcastId = extraDTO.getBroadcastId();
//        this.roomOwner = extraDTO.isRoomOwner();
//    }
//}
