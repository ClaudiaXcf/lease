package com.greatxcf.lease.web.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.greatxcf.lease.common.exception.LeaseException;
import com.greatxcf.lease.common.result.ResultCodeEnum;
import com.greatxcf.lease.model.entity.*;
import com.greatxcf.lease.model.enums.ItemType;
import com.greatxcf.lease.model.enums.ReleaseStatus;
import com.greatxcf.lease.web.admin.mapper.*;
import com.greatxcf.lease.web.admin.service.*;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.greatxcf.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.greatxcf.lease.web.admin.vo.fee.FeeValueVo;
import com.greatxcf.lease.web.admin.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;

    @Autowired
    private ApartmentLabelService apartmentLabelService;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {

        Long apartmentId = apartmentSubmitVo.getId();
        boolean isUpdate = apartmentId != null;
        super.saveOrUpdate(apartmentSubmitVo);  // 调用父类的保存或更新方法，更新公寓实体信息

        // isUpdate = true 更新操作，isUpdate = false 新增操作
        if (isUpdate) {
            // 1.删除配套
            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFacilityQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentId);
            apartmentFacilityService.remove(apartmentFacilityQueryWrapper);

            // 2.删除标签
            LambdaQueryWrapper<ApartmentLabel> apartmentLabelQueryWrapper = new LambdaQueryWrapper<>();
            apartmentLabelQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentId);
            apartmentLabelService.remove(apartmentLabelQueryWrapper);

            // 3.删除杂费
            LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFeeValueQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentId);
            apartmentFeeValueService.remove(apartmentFeeValueQueryWrapper);

            // 4.删除图片
            LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
            graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphQueryWrapper.eq(GraphInfo::getItemId, apartmentId);
            graphInfoService.remove(graphQueryWrapper);
        }

        // 5.插入配套
        List<Long> facilityIds = apartmentSubmitVo.getFacilityInfoIds();
        ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(facilityIds)) {
            facilityIds.forEach(facilityId -> {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentId);
                apartmentFacility.setFacilityId(facilityId);
                facilityList.add(apartmentFacility);
            });

            apartmentFacilityService.saveBatch(facilityList);
        }

        // 6.插入标签
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            ArrayList<ApartmentLabel> labelsList = new ArrayList<>();
            labelIds.forEach(labelId -> {
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setApartmentId(apartmentId);
                apartmentLabel.setLabelId(labelId);
                labelsList.add(apartmentLabel);
            });

            apartmentLabelService.saveBatch(labelsList);
        }

        // 7.插入杂费
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> feeValuesList = new ArrayList<>();
            feeValueIds.forEach(feeValueId -> {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                apartmentFeeValue.setApartmentId(apartmentId);
                apartmentFeeValue.setFeeValueId(feeValueId);
                feeValuesList.add(apartmentFeeValue);
            });

            apartmentFeeValueService.saveBatch(feeValuesList);
        }

        // 8.插入图片
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        // graphVoList不为空的时候给graphInfos赋值
        if (!CollectionUtils.isEmpty(graphVoList)) {

            ArrayList<GraphInfo> graphInfos = new ArrayList<>();
            graphVoList.forEach(graphVo -> {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentId);
                graphInfos.add(graphInfo);
            });

            graphInfoService.saveBatch(graphInfos);
        }
    }

    @Override
    public IPage<ApartmentItemVo> ItemPage(long current, long size, ApartmentQueryVo queryVo) {
        Page<ApartmentInfo> page = new Page<>(current, size);
        return getBaseMapper().itemPage(page, queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        // 1.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);

        // 2.查询图片信息
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
        // 这么操作比较复杂，不仅要创建查询条件，还要再遍历graphInfo之后创建graphVo
//        LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(GraphInfo::getItemType,ItemType.APARTMENT);
//        queryWrapper.eq(GraphInfo::getItemId,id);
//        List<GraphInfo> graphInfos = graphInfoMapper.selectList(queryWrapper);
//        List<GraphVo> graphVos = new ArrayList<>();
//        graphInfos.forEach(graphInfo -> {
//            GraphVo graphVo = new GraphVo(graphInfo.getName(),graphInfo.getUrl());
//            graphVos.add(graphVo);
//        });

        // 3.查询标签信息
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);

        // 4.查询配套信息
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);

        // 5.查询杂费
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);

        // 6.封装数据
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);

        // 返回结果
        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {

        // 先判断该公寓下是否存在房间，存在房间则提示不能删除
        LambdaQueryWrapper<RoomInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long roomCount = roomInfoMapper.selectCount(lambdaQueryWrapper);
        if(roomCount > 0 ){
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }

        // 先删除apartmentInfo信息，再删除其他
        super.removeById(id);

        // 1.删除配套
        LambdaQueryWrapper<ApartmentFacility> apartmentFacilityQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFacilityQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
        apartmentFacilityService.remove(apartmentFacilityQueryWrapper);

        // 2.删除标签
        LambdaQueryWrapper<ApartmentLabel> apartmentLabelQueryWrapper = new LambdaQueryWrapper<>();
        apartmentLabelQueryWrapper.eq(ApartmentLabel::getApartmentId, id);
        apartmentLabelService.remove(apartmentLabelQueryWrapper);

        // 3.删除杂费
        LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFeeValueQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
        apartmentFeeValueService.remove(apartmentFeeValueQueryWrapper);

        // 4.删除图片
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);
    }


}




