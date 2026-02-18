package com.greatxcf.lease.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.greatxcf.lease.model.entity.ViewAppointment;
import com.greatxcf.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.greatxcf.lease.web.admin.vo.appointment.AppointmentVo;

/**
 * @author xcf
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
 * @createDate 2025-07-24 15:48:00
 */
public interface ViewAppointmentService extends IService<ViewAppointment> {

    IPage<AppointmentVo> pageAppointmentVo(Page<AppointmentVo> page, AppointmentQueryVo queryVo);

}
