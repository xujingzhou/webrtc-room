package com.dten.healthcare.room.bean;

import java.io.Serializable;
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
public class StreamsDTO implements Serializable {
    private static final long serialVersionUID = 7209702480946203688L;

    private String streamId;
    private String tracks;
}
