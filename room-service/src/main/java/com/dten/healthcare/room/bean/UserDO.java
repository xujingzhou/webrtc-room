//package com.dten.healthcare.room.bean;
//
//import cn.hutool.core.util.ObjectUtil;
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
// * @Date: 2021/7/30
// * @Description:
// */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@TableName(value = "rm_user")
//public class UserDO implements Serializable {
//    private static final long serialVersionUID = 5011357628897036200L;
//
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @TableField("user_id")
//    private String userId;
//
//    @TableField("user_name")
//    private String userName;
//
//    @TableField("socket_message_Event")
//    private String socketMessageEvent;
//
//    @TableField("socket_custom_event")
//    private String socketCustomEvent;
//
//    @TableField("session_id")
//    private String socketSessionId;
//
//    @TableField("connected_with")
//    private String connectedWith;
//
//    @TableField(value = "video_source_type")
//    private VideoSourceEnum videoSourceType;
//
//    // SocketIOClient内含重复引用，故不能有效序列化/反序列化到库/缓存。
//    // 并且，该项实际也没有保存到数据库的必要性。
//    @TableField("socket")
//    private byte[] client;
//
//    private ExtraDO extra;
//
//    // 为减少查询难度，临时添加此组合查询项(后续可以去除)
//    public CopyOnWriteArrayList<String> connectedWiths = new CopyOnWriteArrayList<>();
//
//    public UserDO(String connectedWith, User user) {
//        this.userId = user.getUserId();
//        this.userName = user.getUserName();
//        this.socketMessageEvent = user.getSocketMessageEvent();
//        this.socketCustomEvent = user.getSocketCustomEvent();
//        if (ObjectUtil.isNotEmpty(user.getClient())) {
////            this.client = ObjectUtil.serialize(user.getClient());
//            this.socketSessionId = user.getClient().getSessionId().toString();
//        }
//        this.connectedWith = connectedWith;
//        this.videoSourceType = VideoSourceEnum.WEBRTC;
//        this.extra = new ExtraDO("", user.getUserId(), user.getExtra());
//        this.connectedWiths = user.getConnectedWith();
//    }
//
//    public UserDO(User user) {
//        this.userId = user.getUserId();
//        this.userName = user.getUserName();
//        this.socketMessageEvent = user.getSocketMessageEvent();
//        this.socketCustomEvent = user.getSocketCustomEvent();
//        if (ObjectUtil.isNotEmpty(user.getClient())) {
////            this.client = ObjectUtil.serialize(user.getClient());
//            this.socketSessionId = user.getClient().getSessionId().toString();
//        }
//        this.videoSourceType = VideoSourceEnum.WEBRTC;
//        this.extra = new ExtraDO("", user.getUserId(), user.getExtra());
//        this.connectedWiths = user.getConnectedWith();
//    }
//}
