package com.dten.healthcare.room.listener;

import com.dten.healthcare.room.handler.IHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/12
 * @Description:
 */
public class HandlerModuleListener {
    private static final HandlerModuleListener moduleListener = new HandlerModuleListener();
    private final List<IHandler> masterListenerList = new ArrayList<>();

    private HandlerModuleListener() {}

    public static HandlerModuleListener getInstance() {
        return moduleListener;
    }

    public void addMasterListener(IHandler clazz) {
        masterListenerList.add(clazz);
    }

    public List<IHandler> getMasterListener() {
        return masterListenerList;
    }
}
