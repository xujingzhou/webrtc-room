//package com.dten.healthcare.room.service;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.dten.healthcare.room.mapper.RoomMapper;
//import com.dten.healthcare.room.common.constant.CacheEnum;
//import com.dten.healthcare.room.utils.EhCacheUtils;
//import com.dten.healthcare.room.bean.ExtraDO;
//import com.dten.healthcare.room.bean.Room;
//import com.dten.healthcare.room.bean.RoomDO;
//import com.dten.healthcare.room.bean.TypeOfStreamsDO;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * @Creator: Johnny Xu
// * @Date: 2021/7/30
// * @Description:
// */
//@Slf4j
//@Service
//@AllArgsConstructor
////@CacheConfig(cacheNames = {"roomCache"})  // 由自动改成手动缓存(encache)方案
//public class RoomService {
//    private final static Logger logger = LoggerFactory.getLogger(RoomService.class);
//
//    @Autowired
//    private RoomMapper roomMapper;
//
//    @Transactional(rollbackFor = Exception.class)
//    public Room get(String roomId) {
//        // get from cache
//        EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//        Room room = (Room)ehCacheUtil.get(CacheEnum.ROOM_CACHE.getName(), roomId);
//
//        // get from db
//        if (ObjectUtil.isEmpty(room)) {
//            room = getRoomByRoomId(roomId);
//            logger.info("RoomService - get() from db");
//
//            // put to cache
//            if (ObjectUtil.isNotEmpty(room)) {
//                ehCacheUtil.put(CacheEnum.ROOM_CACHE.getName(), roomId, room);
//            }
//        } else {
//            logger.info("RoomService - get() from cache");
//        }
//
//        logger.info("RoomService - get(roomId) = {}", roomId);
//        return room;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean save(Room room) {
//        boolean result = false;
//        result = insertExtra(new ExtraDO(room.getRoomId(), room.getExtra()));
//        result = insertStreams(new TypeOfStreamsDO(room.getRoomId(), room.getSession()));
//        result = insertRoom(new RoomDO(room));
//
//        // put to cache
//        if (result) {
//            EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//            ehCacheUtil.put(CacheEnum.ROOM_CACHE.getName(), room.getRoomId(), room);
//        }
//
//        logger.info("RoomService - save(result) = {}", result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean update(Room room) {
//        boolean result = false;
//        if (ObjectUtil.isNotEmpty(room)) {
//            RoomDO roomDO = new RoomDO(room);
//            result = update(roomDO);
//            result = updateExtra(new ExtraDO(room.getRoomId(), room.getExtra()));
//            result = updateStreams(new TypeOfStreamsDO(room.getRoomId(), room.getSession()));
//
//            // delete cache
//            if (result) {
//                EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//                ehCacheUtil.remove(CacheEnum.ROOM_CACHE.getName(), room.getRoomId());
//            }
//        }
//
//        logger.info("RoomService - update(result) = {}", result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean delete(String roomId) {
//        boolean result = deleteByRoomId(roomId);
//
//        // delete cache
//        if (result) {
//            EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//            result = ehCacheUtil.remove(CacheEnum.ROOM_CACHE.getName(), roomId);
//        }
//
//        logger.info("RoomService - delete(roomId) = {}", roomId);
//        return result;
//    }
//
////    @Cacheable(key = "#roomId")
//    private Room getRoomByRoomId(String roomId) {
//        Room roomBO = null;
//        List<RoomDO> listRoomDO = null;
//        listRoomDO = roomMapper.getRoomByRoomId(roomId);
//
//        if (ObjectUtil.isNotEmpty(listRoomDO) && listRoomDO.size() > 0) {
//            RoomDO roomDO = listRoomDO.get(0);
//            roomBO = new Room(roomDO);
//        }
//
//        logger.info("RoomService - getRoomByRoomId(roomId) = {}", roomId);
//        return roomBO;
//    }
//
//    private List<RoomDO> getRoomByParticipant(String roomId, String participant) {
//        List<RoomDO> result = roomMapper.getRoomByParticipant(roomId, participant);
//        logger.info("RoomService - getRoomByParticipant(roomId) = {}", roomId);
//        return result;
//    }
//
////    @CachePut(key = "#room.roomId")
//    private boolean update(RoomDO room) {
//        boolean result = roomMapper.update(room);
//        logger.info("RoomService - private update(result) = {}", result);
//        return result;
//    }
//
////    @CachePut(key = "#room.roomId")
//    private boolean updateRoom(String uid, RoomDO room) {
//        boolean result = roomMapper.updateRoom(uid, room);
//        logger.info("RoomService - updateRoom(result) = {}", result);
//        return result;
//    }
//
//    private boolean updateExtra(ExtraDO extra) {
//        boolean result = roomMapper.updateExtra(extra);
//        logger.info("RoomService - updateExtra(result) = {}", result);
//        return result;
//    }
//
//    private boolean updateStreams(TypeOfStreamsDO streams) {
//        boolean result = roomMapper.updateStreams(streams);
//        logger.info("RoomService - updateStreams(result) = {}", result);
//        return result;
//    }
//
////    @CachePut(key = "#room.roomId")
//    private boolean insertRoom(RoomDO room) {
//        boolean result = roomMapper.insertRoom(room);
//        logger.info("RoomService - insertRoom(result) = {}", result);
//        return result;
//    }
//
//    private boolean insertExtra(ExtraDO extra) {
//        boolean result = roomMapper.insertExtra(extra);
//        logger.info("RoomService - insertExtra(result) = {}", result);
//        return result;
//    }
//
//    private boolean insertStreams(TypeOfStreamsDO streams) {
//        boolean result = roomMapper.insertStreams(streams);
//        logger.info("RoomService - insertStreams(result) = {}", result);
//        return result;
//    }
//
////    @CacheEvict(key = "#roomId")
//    private boolean deleteByRoomId(String roomId) {
//        boolean result = roomMapper.deleteByRoomId(roomId);
//        logger.info("RoomService - deleteByRoomId(result) = {}", result);
//
//        // another call way
////        SqlSession sqlSession = DBMybatisUtils.getSqlsessionfactory().openSession();
////        try {
////            RoomMapper roomMapper = sqlSession.getMapper(RoomMapper.class);
////            result = roomMapper.deleteByRoomId(roomId);
////            sqlSession.commit();
////
////        } catch (Exception e) {
////            logger.info("RoomService - deleteByRoomId exception = {}", e.toString());
////        } finally {
////            sqlSession.close();
////        }
//
//        return result;
//    }
//}
