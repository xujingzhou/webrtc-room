package com.dten.healthcare.room.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
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
public class MessageInnerDTO implements Serializable {
    private static final long serialVersionUID = 6914040790386672367L;

    private String userId;

    private boolean userLeft;

    private boolean detectPresence;

    private boolean newParticipationRequest;

    private boolean enableMedia;

    private boolean readyForOffer;

    @JSONField(jsonDirect=true)
    private String userPreferences;

    @JSONField(name="isOneWay")
    private boolean oneWay;

    @JSONField(name="isDataOnly")
    private boolean dataOnly;

    private boolean autoCloseEntireSession;

    private boolean renegotiatingPeer;
    private boolean dontGetRemoteStream;

    private TypeOfStreamsDTO localPeerSdpConstraints;
    private TypeOfStreamsDTO remotePeerSdpConstraints;

    private TypeOfStreamsDTO session;

    private String sessionId;

    private String type;
    private String sdp;
    private String candidate;
    private String sdpMid;
    private String sdpMLineIndex;
    @JSONField(jsonDirect=true)
    private String streamsToShare;
    @JSONField(jsonDirect=true)
    private String connectionDescription;

    private String mediaConstraints;
    private String sdpConstraints;

    private String identifier;
    private String password;

    private ExtraDTO extra;

    private List<StreamsDTO> streams = new ArrayList<>();
    public List<String> allParticipants = new ArrayList<>();
}
