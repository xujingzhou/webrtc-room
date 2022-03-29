package com.dten.healthcare.room.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    private static final long serialVersionUID = -751362879460342873L;

    private String roomId;

    private String roomName;

    private String owner;

    private String identifier;

    private int maxParticipantsAllowed;

    private TypeOfStreamsDTO session;

    private ExtraDTO extra;

    public CopyOnWriteArrayList<String> participants = new CopyOnWriteArrayList<>();

//    public Room(RoomDO roomDO) {
//        this.roomId = roomDO.getRoomId();
//        this.roomName = roomDO.getRoomName();
//        this.owner = roomDO.getOwner();
//        this.identifier = roomDO.getIdentifier();
//        this.maxParticipantsAllowed = roomDO.getMaxParticipantsAllowed();
//        this.session = new TypeOfStreamsDTO(roomDO.getSession());
//        this.extra = new ExtraDTO(roomDO.getExtra());
//        this.participants = roomDO.getParticipants();
//    }
}
