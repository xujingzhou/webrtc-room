package com.dten.healthcare.room.bean;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/30
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 5234625968093802748L;

    private String userId;

    private String userName;

    private String socketMessageEvent;

    private String socketCustomEvent;

    private SocketIOClient client;

    private ExtraDTO extra;

    public CopyOnWriteArrayList<String> connectedWith = new CopyOnWriteArrayList<>();

//    public User(UserDO userDo) {
//        this.userId = userDo.getUserId();
//        this.userName = userDo.getUserName();
//        this.socketMessageEvent = userDo.getSocketMessageEvent();
//        this.socketCustomEvent = userDo.getSocketCustomEvent();
////        if (ObjectUtil.isNotEmpty(userDo.getClient())) {
////            this.client = ObjectUtil.deserialize(userDo.getClient());
////        }
//        this.extra = new ExtraDTO(userDo.getExtra());
//        this.connectedWith = userDo.getConnectedWiths();
//    }
}
