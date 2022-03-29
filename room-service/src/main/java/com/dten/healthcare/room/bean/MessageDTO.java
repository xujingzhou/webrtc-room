package com.dten.healthcare.room.bean;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -2718869651811989430L;

    // 发送方id
    private String sender;

    // 目标用户id
    private String remoteUserId;

    // 用户id
    private String userId;

    // 房间主持人id
    private String broadcastId;

    private TypeOfStreamsDTO typeOfStreams;

    private ExtraDTO extra;

    private MessageInnerDTO message;

    private PeerStateDTO states;
}
