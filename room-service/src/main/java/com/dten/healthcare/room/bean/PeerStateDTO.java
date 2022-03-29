package com.dten.healthcare.room.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.dten.healthcare.room.common.constant.IceConnectionStateEnum;
import com.dten.healthcare.room.common.constant.IceGatheringStateEnum;
import com.dten.healthcare.room.common.constant.SignalingStateEnum;
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
public class PeerStateDTO implements Serializable {
    private static final long serialVersionUID = 5691681105903382993L;

    @JSONField(name="userid")
    private String userId;

    private IceConnectionStateEnum iceConnectionState;
    private IceGatheringStateEnum iceGatheringState;
    private SignalingStateEnum signalingState;

    private ExtraDTO extra;
}
