package com.dten.healthcare.room.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraDTO implements Serializable {
    private static final long serialVersionUID = 1694600848223005480L;

    private String userFullName;

    private String broadcastId;

    private boolean roomOwner;

//    public ExtraDTO(ExtraDO extraDO) {
//        this.userFullName = extraDO.getUserFullName();
//        this.broadcastId = extraDO.getBroadcastId();
//        this.roomOwner = extraDO.isRoomOwner();
//    }
}
