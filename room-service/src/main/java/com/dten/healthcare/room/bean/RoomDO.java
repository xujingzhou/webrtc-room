//package com.dten.healthcare.room.bean;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.dten.healthcare.room.common.constant.VideoSourceEnum;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.util.concurrent.CopyOnWriteArrayList;
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
//@TableName(value = "rm_room")
//public class RoomDO implements Serializable {
//    private static final long serialVersionUID = -3534085243474234850L;
//
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @TableField("room_id")
//    private String roomId;
//
//    @TableField(value = "room_name")
//    private String roomName;
//
//    @TableField(value = "max_participants_Allowed")
//    private int maxParticipantsAllowed;
//
//    private String owner;
//
//    private String identifier;
//
//    private TypeOfStreamsDO session;
//
//    private ExtraDO extra;
//
//    private String participant;
//
//    @TableField(value = "video_source_type")
//    private VideoSourceEnum videoSourceType;
//
//    // 为减少查询难度，临时添加此组合查询项(后续可以去除)
//    public CopyOnWriteArrayList<String> participants = new CopyOnWriteArrayList<>();
//
//    public RoomDO(String participant, Room room) {
//        this.roomId = room.getRoomId();
//        this.roomName = room.getRoomName();
//        this.maxParticipantsAllowed = room.getMaxParticipantsAllowed();
//        this.owner = room.getOwner();
//        this.identifier = room.getIdentifier();
//        this.participant = participant;
//        this.session = new TypeOfStreamsDO(room.getRoomId(), room.getSession());
//        this.extra = new ExtraDO(room.getRoomId(), "", room.getExtra());
//        this.participants = room.getParticipants();
//        this.videoSourceType = VideoSourceEnum.WEBRTC;
//    }
//
//    public RoomDO(Room room) {
//        this.roomId = room.getRoomId();
//        this.roomName = room.getRoomName();
//        this.maxParticipantsAllowed = room.getMaxParticipantsAllowed();
//        this.owner = room.getOwner();
//        this.identifier = room.getIdentifier();
//        this.session = new TypeOfStreamsDO(room.getRoomId(), room.getSession());
//        this.extra = new ExtraDO(room.getRoomId(), "", room.getExtra());
//        this.participants = room.getParticipants();
//        this.videoSourceType = VideoSourceEnum.WEBRTC;
//    }
//}
