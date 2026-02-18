package com.greatxcf.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greatxcf.lease.common.result.Result;
import com.greatxcf.lease.model.entity.RoomInfo;
import com.greatxcf.lease.model.enums.ReleaseStatus;
import com.greatxcf.lease.web.admin.service.RoomInfoService;
import com.greatxcf.lease.web.admin.vo.room.RoomDetailVo;
import com.greatxcf.lease.web.admin.vo.room.RoomItemVo;
import com.greatxcf.lease.web.admin.vo.room.RoomQueryVo;
import com.greatxcf.lease.web.admin.vo.room.RoomSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间信息管理")
@RestController
@RequestMapping("/admin/room")
public class RoomController {

    @Autowired
    private RoomInfoService roomInfoService;

    @Operation(summary = "保存或更新房间信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        roomInfoService.saveOrUpdateRoomInfo(roomSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("pageItem")
    public Result<IPage<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size,
            RoomQueryVo queryVo) {
        Page<RoomItemVo> page = new Page<>(current, size);
        IPage<RoomItemVo> iPage = roomInfoService.pageItem(page, queryVo);
        return Result.ok(iPage);
    }

    @Operation(summary = "根据id获取房间详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        RoomDetailVo roomDetailVo = roomInfoService.getDetailById(id);
        return Result.ok(roomDetailVo);
    }

    @Operation(summary = "根据id删除房间信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        roomInfoService.removeRoomById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改房间发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        LambdaUpdateWrapper<RoomInfo> rooLambdaQueryWrapper = new LambdaUpdateWrapper<RoomInfo>();
        rooLambdaQueryWrapper.eq(RoomInfo::getId, id);
        rooLambdaQueryWrapper.set(RoomInfo::getIsRelease, status);
        roomInfoService.update(rooLambdaQueryWrapper);
        return Result.ok();
    }

    @GetMapping("listBasicByApartmentId")
    @Operation(summary = "根据公寓id查询房间列表")
    public Result<List<RoomInfo>> listBasicByApartmentId(Long id) {
        LambdaQueryWrapper<RoomInfo> roomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        roomLambdaQueryWrapper.eq(RoomInfo::getIsRelease,ReleaseStatus.RELEASED);
        List<RoomInfo> result = roomInfoService.list(roomLambdaQueryWrapper);
        return Result.ok(result);
    }
}
