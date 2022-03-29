package com.dten.healthcare.room.common;

import lombok.*;
import java.util.Map;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/3
 * @Description:
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventData {
    private String eventName;
    private Map<String, Object> data;
}
