package com.dten.healthcare.room.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.*;
import com.dten.healthcare.room.common.Callback;
import com.dten.healthcare.room.common.constant.ClientEvent;
import com.dten.healthcare.room.bean.*;
import com.dten.healthcare.room.config.AsyncTaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.core.util.ObjectUtil;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/8/11
 * @Description:
 */
@Slf4j
@Service
public class TestHandler {
    @Autowired
    private final SocketIOServer server;
    private final int heartbeatTimeout = 6; // seconds
    private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private AsyncTaskConfig asyncTask;

//    @Autowired
//    RoomService roomService;
//    @Autowired
//    UserService userService;

    @Autowired
    public TestHandler(SocketIOServer server) {
        this.server = server;
    }

    // ----------------------------------------------------------
    // test NettySocketIO
    @OnEvent(value = "test")
    public void test(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("test = {}", data);

        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("Hi, johnny");
        }

        if (StrUtil.isNotEmpty(data)) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            MessageDTO message = JSONObject.toJavaObject(jsonObject, MessageDTO.class);

            String userId = message.getUserId();
            if (StrUtil.isNotEmpty(message.getUserId())) {
                MessageDTO sendData = new MessageDTO();
                sendData.setUserId(userId);
                sendData.setRemoteUserId(message.getRemoteUserId());
                client.sendEvent(ClientEvent.TEST, sendData);
                logger.info("test: sendData = {}", JSON.toJSONString(sendData));
            }
        }
    }

    @OnEvent(value = "test-voidack")
    public void testVoidAck(SocketIOClient client, AckRequest ackRequest, String message) {
        logger.info("testVoidAck = {}", message);

        try {
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(true);
            }

            String eventName = "test-voidack";
            if (ObjectUtil.isNotEmpty(client)) {
                client.sendEvent(eventName, new VoidAckCallback(heartbeatTimeout) {
                    @Override
                    protected void onSuccess() {
                        logger.info("--- testVoidAck: onSuccess()");
                    }

                    public void onTimeout() {
                        logger.info("--- testVoidAck: onTimeout()");
                    }
                }, message);
            }

        } catch (Exception e) {
            logger.info("testVoidAck Exception = {}", e.toString());
        }
    }

    @OnEvent(value = "test-ack")
    public void testAck(SocketIOClient client, AckRequest ackRequest, String message) throws ExecutionException, InterruptedException {
        logger.info("testAck = {}", message);

        // test db
//        testUser_Get();
//        testUser_Save(client);
//        testUser_Get();
//        testUser_Update(client);
//        testUser_Get();
//        testUser_Delete();
//
//        testRoom_Get();
//        testRoom_Save();
//        testRoom_Get();
//        testRoom_Update();
//        testRoom_Get();
//        testRoom_Delete();

        sendMsgWithVoidAck(client, "test-ack", message, new Callback<Integer>(Integer.class) {
            @Override
            public void onSuccess(Integer result) {
                logger.info("sendMsgWithVoidAck callback - onSuccess: result = {}", result.toString());
            }

            @Override
            public void onFail(Integer result) {
                logger.info("sendMsgWithVoidAck callback - onFail: result = {}", result.toString());
            }
        });

//        try {
//            if (ackRequest.isAckRequested()) {
//                ackRequest.sendAckData(true);
//            }
//
//            String eventName = "test-ack";
//            if (ObjectUtil.isNotEmpty(client)) {
//                // 第一次和重试时执行(通过异常触发重试)
//                final RetryCallback<Object, Exception> retryCallback = new RetryCallback<Object, Exception>() {
//                    @Override
//                    public Object doWithRetry(RetryContext context) throws Exception {
//                        logger.info("--- testAck: doWithRetry() getRetryCount = {}", context.getRetryCount());
//                        logger.info("--- testAck: doWithRetry() eventName = {}", eventName);
//                        throw new SocketTimeoutException("--- testAck: doWithRetry() SocketTimeoutException");
//                    }
//                };
//
//                // 重试流程正常结束或达到重试上限后的退出恢复操作
//                final RecoveryCallback<Object> recoveryCallback = new RecoveryCallback<Object>() {
//                    @Override
//                    public Object recover(RetryContext context) throws Exception {
//                        logger.info("--- testAck: recover() getRetryCount = {}", context.getRetryCount());
//                        return null;
//                    }
//                };
//
//                client.sendEvent(eventName, new AckCallback<String>(String.class, heartbeatTimeout) {
//                    @Override
//                    public void onSuccess(String result) {
//                        if (StrUtil.isEmpty(result))
//                            logger.info("--- testAck: onSuccess()");
//                        else
//                            logger.info("--- testAck: onSuccess() = {}", result);
//                    }
//
//                    @Override
//                    public void onTimeout() {
//                        logger.info("--- testAck: onTimeout()");
//
//                        asyncTask.getAsyncExecutor().execute(() -> {
//                            // 由retryTemplate 执行execute方法开始逻辑执行
//                            try {
//                                retryTemplate.execute(retryCallback, recoveryCallback);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//                }, message); }
//
//        } catch (Exception e) {
//            logger.info("testAck Exception = {}", e.toString());
//        }
    }

    private void sendMsgWithVoidAck(SocketIOClient client, String eventName, String message, Callback<Integer> callback) {
        final boolean[] success = {false};
        if (ObjectUtil.isNotEmpty(client)) {
            client.sendEvent(eventName, new AckCallback<String>(String.class) {
                @Override
                public void onSuccess(String result) {
                    success[0] = true;
                    logger.info("--- SocketIOTest: sendMsgWithVoidAck() onSuccess");

                    // 回调返回
                    callback.onSuccess(1);
                }

                public void onTimeout() {
                    success[0] = false;
                    logger.info("--- SocketIOTest: sendMsgWithVoidAck() - onTimeout");

                    // 回调返回
                    callback.onFail(0);
                }
            }, message);
        }
    }

    // ----------------------------------------------------------

    // ----------------------------------------------------------
    // test DB
//    public void testRoom_Save() {
//        ExtraDTO extra = new ExtraDTO();
//        extra.setBroadcastId("ddd");
//        TypeOfStreamsDTO streams = new TypeOfStreamsDTO();
//        streams.setAudio(true);
//        streams.setVideo(true);
//        CopyOnWriteArrayList<String> participants = new CopyOnWriteArrayList<>();
//        participants.add("345");
//        participants.add("678");
//        Room room = new Room();
//        room.setRoomId("123");
//        room.setRoomName("abc");
//        room.setMaxParticipantsAllowed(100);
//        room.setOwner("345");
//        room.setIdentifier("test-1");
//        room.setExtra(extra);
//        room.setSession(streams);
//        room.setParticipants(participants);
//
//        try {
//            boolean success = roomService.save(room);
//            logger.info("testRoomSave = {}", success);
//        } catch (Exception e) {
//            logger.info("testRoomSave exception = {}", e.toString());
//        }
//    }
//
//    public Room testRoom_Get() {
//        Room result = null;
//        try {
//            result = roomService.get("123");
//            logger.info("testRoomGet = {}", JSON.toJSONString(result));
//        } catch (Exception e){
//            logger.info("testRoomGet exception = {}", e.toString());
//        }
//
//        return result;
//    }
//
//    public void testRoom_Update() {
//        ExtraDTO extra = new ExtraDTO();
//        extra.setBroadcastId("xujingzhou");
//        TypeOfStreamsDTO streams = new TypeOfStreamsDTO();
//        streams.setAudio(false);
//        streams.setVideo(true);
//        streams.setBroadcast(true);
//        CopyOnWriteArrayList<String> participants = new CopyOnWriteArrayList<>();
//        participants.add("345");
//        participants.add("678");
//        participants.add("012");
//        Room room = new Room();
//        room.setRoomId("123");
//        room.setRoomName("johnny");
//        room.setMaxParticipantsAllowed(100);
//        room.setOwner("012");
//        room.setIdentifier("test");
//        room.setExtra(extra);
//        room.setSession(streams);
//        room.setParticipants(participants);
//
//        try {
//            boolean success = roomService.update(room);
//            logger.info("testRoomUpdate = {}", success);
//        } catch (Exception e) {
//            logger.info("testRoomUpdate exception = {}", e.toString());
//        }
//    }
//
//    public void testRoom_Delete() {
//        try {
//            boolean success = roomService.delete("123");
//            logger.info("testRoomDelete = {}", success);
//        } catch (Exception e) {
//            logger.info("testRoomDelete exception = {}", e.toString());
//        }
//    }
//
//    ////////////////////////////////
//    public void testUser_Save(SocketIOClient client) {
//        ExtraDTO extra = new ExtraDTO();
//        extra.setBroadcastId("ddd");
//        CopyOnWriteArrayList<String> connectedWith = new CopyOnWriteArrayList<>();
//        connectedWith.add("345");
//        connectedWith.add("678");
//        User user = new User();
//        user.setUserId("123");
//        user.setUserName("abc");
//        user.setSocketCustomEvent("msgEvent");
//        user.setExtra(extra);
//        user.setConnectedWith(connectedWith);
//        if (ObjectUtil.isNotEmpty(client)) {
//            user.setClient(client);
//        }
//
//        try {
//            boolean success = userService.save(user);
//            logger.info("testUserSave = {}", success);
//        } catch (Exception e) {
//            logger.info("testUserSave exception = {}", e.toString());
//        }
//    }
//
//    public User testUser_Get() {
//        User result = null;
//        try {
//            result = userService.get("123");
//            logger.info("testUserGet = {}", JSON.toJSONString(result));
//        } catch (Exception e) {
//            logger.info("testUserGet exception = {}", e.toString());
//        }
//
//        return result;
//    }
//
//    public void testUser_Update(SocketIOClient client) {
//        ExtraDTO extra = new ExtraDTO();
//        extra.setBroadcastId("xujingzhou");
//        extra.setRoomOwner(true);
//        CopyOnWriteArrayList<String> connectedWith = new CopyOnWriteArrayList<>();
//        connectedWith.add("345");
//        connectedWith.add("678");
//        connectedWith.add("012");
//        User user = new User();
//        user.setUserId("123");
//        user.setUserName("johnny111");
//        user.setSocketMessageEvent("msg");
//        user.setExtra(extra);
//        user.setConnectedWith(connectedWith);
//        if (ObjectUtil.isNotEmpty(client)) {
//            user.setClient(client);
//        }
//
//        try {
//            boolean success = userService.update(user);
//            logger.info("testUserUpdate = {}", success);
//        } catch (Exception e) {
//            logger.info("testUserUpdate exception = {}", e.toString());
//        }
//    }
//
//    public void testUser_Delete() {
//        try {
//            boolean success = userService.delete("123");
//            logger.info("testUserDelete = {}", success);
//        } catch (Exception e) {
//            logger.info("testUserDelete exception = {}", e.toString());
//        }
//    }

    // ----------------------------------------------------------
}
