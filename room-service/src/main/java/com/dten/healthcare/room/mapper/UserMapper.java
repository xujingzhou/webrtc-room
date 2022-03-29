//package com.dten.healthcare.room.mapper;
//
//import com.dten.healthcare.room.bean.ExtraDO;
//import com.dten.healthcare.room.bean.UserDO;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Mapper
//public interface UserMapper extends BaseMapper<UserDO> {
//    public List<UserDO> getUserByUserId(@Param("userId") String userId);
//    public List<UserDO> getUserByConnectedWith(@Param("userId") String userId, @Param("connectedWith") String connectedWith);
//
//    public boolean update(@Param("user") UserDO user);
//    public boolean updateUser(@Param("connectedWith") String connectedWith, @Param("user") UserDO user);
//    public boolean updateExtra(@Param("extra") ExtraDO extra);
//
//    public boolean insertUser(@Param("user") UserDO user);
//    public boolean insertExtra(@Param("extra") ExtraDO extra);
//
//    public boolean insertOrUpdateUser(@Param("user") UserDO user);
//    public boolean insertOrUpdateExtra(@Param("extra") ExtraDO extra);
//
//    public boolean deleteByUserId(@Param("userId") String userId);
//}
