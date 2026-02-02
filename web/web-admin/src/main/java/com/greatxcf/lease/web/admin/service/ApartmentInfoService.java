package com.greatxcf.lease.web.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.greatxcf.lease.model.entity.ApartmentInfo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentSubmitVo;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ApartmentInfoService extends IService<ApartmentInfo> {

    void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    IPage<ApartmentItemVo> ItemPage(long current, long size, ApartmentQueryVo queryVo);

    ApartmentDetailVo getDetailById(Long id);

    void removeApartmentById(Long id);

}
