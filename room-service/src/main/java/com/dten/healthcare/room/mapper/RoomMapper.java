//package com.dten.healthcare.room.mapper;
//
//import com.dten.healthcare.room.bean.ExtraDO;
//import com.dten.healthcare.room.bean.RoomDO;
//import com.dten.healthcare.room.bean.TypeOfStreamsDO;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Mapper
//public interface RoomMapper extends BaseMapper<RoomDO> {
//    public List<RoomDO> getRoomByRoomId(@Param("roomId") String roomId);
//    public List<RoomDO> getRoomByParticipant(@Param("roomId") String roomId, @Param("participant") String participant);
//
//    public boolean update(@Param("room") RoomDO room);
//    public boolean updateRoom(@Param("participant") String participant, @Param("room") RoomDO room);
//    public boolean updateExtra(@Param("extra") ExtraDO extra);
//    public boolean updateStreams(@Param("streams") TypeOfStreamsDO streams);
//
//    public boolean insertRoom(@Param("room") RoomDO room);
//    public boolean insertExtra(@Param("extra") ExtraDO extra);
//    public boolean insertStreams(@Param("streams") TypeOfStreamsDO streams);
//
//    public boolean insertOrUpdateRoom(@Param("room") RoomDO room);
//    public boolean insertOrUpdateExtra(@Param("extra") ExtraDO extra);
//    public boolean insertOrUpdateStreams(@Param("streams") TypeOfStreamsDO streams);
//
//    public boolean deleteByRoomId(@Param("roomId") String roomId);
//}
