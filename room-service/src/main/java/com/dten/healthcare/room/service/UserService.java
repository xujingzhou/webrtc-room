//package com.dten.healthcare.room.service;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.dten.healthcare.room.mapper.UserMapper;
//import com.dten.healthcare.room.common.constant.CacheEnum;
//import com.dten.healthcare.room.utils.EhCacheUtils;
//import com.dten.healthcare.room.bean.ExtraDO;
//import com.dten.healthcare.room.bean.User;
//import com.dten.healthcare.room.bean.UserDO;
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
//public class UserService {
//    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Transactional(rollbackFor = Exception.class)
//    public User get(String userId) {
//        // get from cache
//        EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//        User user = (User)ehCacheUtil.get(CacheEnum.User_CACHE.getName(), userId);
//
//        // get from db
//        if (ObjectUtil.isEmpty(user)) {
//            user = getUserByUserId(userId);
//            logger.info("UserService - get() from db");
//
//            // put to cache
//            if (ObjectUtil.isNotEmpty(user)) {
//                ehCacheUtil.put(CacheEnum.User_CACHE.getName(), userId, user);
//            }
//        } else {
//            logger.info("UserService - get() from cache");
//        }
//
//        logger.info("UserService - get(userId) = {}", userId);
//        return user;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean save(User user) {
//        boolean result = false;
//        result = insertExtra(new ExtraDO("", user.getUserId(), user.getExtra()));
//        result = insertUser(new UserDO(user));
//
//        // put to cache
//        if (result) {
//            EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//            ehCacheUtil.put(CacheEnum.User_CACHE.getName(), user.getUserId(), user);
//        }
//
//        logger.info("UserService - save(result) = {}", result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean update(User user) {
//        boolean result = false;
//        if (ObjectUtil.isNotEmpty(user)) {
//            UserDO userDO = new UserDO(user);
//            result = update(userDO);
//            result = updateExtra(new ExtraDO("", user.getUserId(), user.getExtra()));
//
//            // delete cache
//            if (result) {
//                EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//                ehCacheUtil.remove(CacheEnum.User_CACHE.getName(), user.getUserId());
//            }
//        }
//
//        logger.info("UserService - update(result) = {}", result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean delete(String userId) {
//        boolean result = deleteByUserId(userId);
//
//        // delete cache
//        if (result) {
//            EhCacheUtils ehCacheUtil = EhCacheUtils.getInstance();
//            result = ehCacheUtil.remove(CacheEnum.User_CACHE.getName(), userId);
//        }
//
//        logger.info("UserService - delete(userId) = {}", userId);
//        return result;
//    }
//
//    private User getUserByUserId(String userId) {
//        User userBO = null;
//        List<UserDO> listUserDO = null;
//        listUserDO = userMapper.getUserByUserId(userId);
//
//        if (ObjectUtil.isNotEmpty(listUserDO) && listUserDO.size() > 0) {
//            UserDO userDO = listUserDO.get(0);
//            userBO = new User(userDO);
//        }
//
//        logger.info("UserService - getUserByUserId(userId) = {}", userId);
//        return userBO;
//    }
//
//    private List<UserDO> getUserByConnectedWith(String userId, String connectedWith) {
//        List<UserDO> result = userMapper.getUserByConnectedWith(userId, connectedWith);
//        logger.info("UserService - getUserByConnectedWith(userId) = {}", userId);
//        return result;
//    }
//
//    private boolean update(UserDO user) {
//        boolean result = userMapper.update(user);
//        logger.info("UserService - private update(result) = {}", result);
//        return result;
//    }
//
//    private boolean updateUser(String uid, UserDO user) {
//        boolean result = userMapper.updateUser(uid, user);
//        logger.info("UserService - updateUser(result) = {}", result);
//        return result;
//    }
//
//    private boolean updateExtra(ExtraDO extra) {
//        boolean result = userMapper.updateExtra(extra);
//        logger.info("UserService - updateExtra(result) = {}", result);
//        return result;
//    }
//
//    private boolean insertUser(UserDO user) {
//        boolean result = userMapper.insertUser(user);
//        logger.info("UserService - insertUser(result) = {}", result);
//        return result;
//    }
//
//    private boolean insertExtra(ExtraDO extra) {
//        boolean result = userMapper.insertExtra(extra);
//        logger.info("UserService - insertExtra(result) = {}", result);
//        return result;
//    }
//
//    private boolean deleteByUserId(String userId) {
//        boolean result = userMapper.deleteByUserId(userId);
//        logger.info("UserService - deleteByUserId(result) = {}", result);
//
//        // another call way
////        SqlSession sqlSession = DBMybatisUtils.getSqlsessionfactory().openSession();
////        try {
////            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
////            result = userMapper.deleteByUserId(userId);
////            sqlSession.commit();
////
////        } catch (Exception e) {
////            logger.info("UserService - deleteByUserId exception = {}", e.toString());
////        } finally {
////            sqlSession.close();
////        }
//
//        return result;
//    }
//}
