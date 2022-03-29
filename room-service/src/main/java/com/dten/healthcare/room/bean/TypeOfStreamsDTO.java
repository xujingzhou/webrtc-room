package com.dten.healthcare.room.bean;

import com.alibaba.fastjson.annotation.JSONField;
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
public class TypeOfStreamsDTO implements Serializable {
    private static final long serialVersionUID = -8876850021420241436L;

    @JSONField(name="OfferToReceiveAudio")
    private boolean offerToReceiveAudio;

    @JSONField(name="OfferToReceiveVideo")
    private boolean offerToReceiveVideo;

    private boolean audio;

    private boolean video;

    private boolean screen;

    private boolean oneway;

    private boolean broadcast;

    private boolean scalable;

//    public TypeOfStreamsDTO(TypeOfStreamsDO streamsDO) {
//        this.offerToReceiveAudio = streamsDO.isOfferToReceiveAudio();
//        this.offerToReceiveVideo = streamsDO.isOfferToReceiveVideo();
//        this.audio = streamsDO.isAudio();
//        this.video = streamsDO.isVideo();
//        this.screen = streamsDO.isScreen();
//        this.oneway = streamsDO.isOneway();
//        this.broadcast = streamsDO.isBroadcast();
//        this.scalable = streamsDO.isScalable();
//    }
}
