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
//import java.io.Serializable;
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
//@TableName(value = "rm_type_of_streams")
//public class TypeOfStreamsDO implements Serializable {
//    private static final long serialVersionUID = -6805899217392956369L;
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
//    @TableField(value = "offer_to_receive_audio")
//    private boolean offerToReceiveAudio;
//
//    @TableField(value = "offer_to_receive_video")
//    private boolean offerToReceiveVideo;
//
//    private boolean audio;
//
//    private boolean video;
//
//    private boolean screen;
//
//    private boolean oneway;
//
//    private boolean broadcast;
//
//    private boolean scalable;
//
//    public TypeOfStreamsDO(String roomId, String userId, TypeOfStreamsDTO steamsDTO) {
//        this.roomId = roomId;
//        this.userId = userId;
//        this.offerToReceiveAudio = steamsDTO.isOfferToReceiveAudio();
//        this.offerToReceiveVideo = steamsDTO.isOfferToReceiveVideo();
//        this.audio = steamsDTO.isAudio();
//        this.video = steamsDTO.isVideo();
//        this.screen = steamsDTO.isScreen();
//        this.oneway = steamsDTO.isOneway();
//        this.broadcast = steamsDTO.isBroadcast();
//        this.scalable = steamsDTO.isScalable();
//    }
//
//    public TypeOfStreamsDO(String roomId, TypeOfStreamsDTO steamsDTO) {
//        this.roomId = roomId;
//        this.offerToReceiveAudio = steamsDTO.isOfferToReceiveAudio();
//        this.offerToReceiveVideo = steamsDTO.isOfferToReceiveVideo();
//        this.audio = steamsDTO.isAudio();
//        this.video = steamsDTO.isVideo();
//        this.screen = steamsDTO.isScreen();
//        this.oneway = steamsDTO.isOneway();
//        this.broadcast = steamsDTO.isBroadcast();
//        this.scalable = steamsDTO.isScalable();
//    }
//}
