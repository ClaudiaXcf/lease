package com.greatxcf.lease.web.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.greatxcf.lease.model.entity.RoomInfo;
import com.greatxcf.lease.web.admin.vo.room.RoomDetailVo;
import com.greatxcf.lease.web.admin.vo.room.RoomItemVo;
import com.greatxcf.lease.web.admin.vo.room.RoomQueryVo;
import com.greatxcf.lease.web.admin.vo.room.RoomSubmitVo;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface RoomInfoService extends IService<RoomInfo> {

    void saveOrUpdateRoomInfo(RoomSubmitVo roomSubmitVo);

    IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo);

    RoomDetailVo getDetailById(Long id);
}
