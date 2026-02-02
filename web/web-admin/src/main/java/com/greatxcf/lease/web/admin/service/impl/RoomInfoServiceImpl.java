package com.greatxcf.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.greatxcf.lease.model.entity.*;
import com.greatxcf.lease.model.enums.ItemType;
import com.greatxcf.lease.web.admin.mapper.*;
import com.greatxcf.lease.web.admin.service.*;
import com.greatxcf.lease.web.admin.vo.attr.AttrValueVo;
import com.greatxcf.lease.web.admin.vo.graph.GraphVo;
import com.greatxcf.lease.web.admin.vo.room.RoomDetailVo;
import com.greatxcf.lease.web.admin.vo.room.RoomItemVo;
import com.greatxcf.lease.web.admin.vo.room.RoomQueryVo;
import com.greatxcf.lease.web.admin.vo.room.RoomSubmitVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private RoomAttrValueService roomAttrValueService;

    @Autowired
    private RoomFacilityService roomFacilityService;

    @Autowired
    private RoomLabelService roomLabelService;

    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;

    @Autowired
    private RoomLeaseTermService roomLeaseTermService;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Override
    public void saveOrUpdateRoomInfo(RoomSubmitVo roomSubmitVo) {

        Long roomId = roomSubmitVo.getId();
        boolean isUpdate = roomId != null;
        super.saveOrUpdate(roomSubmitVo);

        if (isUpdate) {
            // 删除所有信息重新插入
            // 1.删除图片信息
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, roomId);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

            // 2.删除属性信息
            LambdaQueryWrapper<RoomAttrValue> valueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            valueLambdaQueryWrapper.eq(RoomAttrValue::getRoomId, roomId);
            roomAttrValueService.remove(valueLambdaQueryWrapper);

            // 3.删除配套信息
            LambdaQueryWrapper<RoomFacility> facilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            facilityLambdaQueryWrapper.eq(RoomFacility::getRoomId, roomId);
            roomFacilityService.remove(facilityLambdaQueryWrapper);

            // 4.删除标签信息
            LambdaQueryWrapper<RoomLabel> labelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            labelLambdaQueryWrapper.eq(RoomLabel::getRoomId, roomId);
            roomLabelService.remove(labelLambdaQueryWrapper);

            // 5.删除支付方式
            LambdaQueryWrapper<RoomPaymentType> typeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            typeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId, roomId);
            roomPaymentTypeService.remove(typeLambdaQueryWrapper);

            // 6.删除可选租期
            LambdaQueryWrapper<RoomLeaseTerm> termLambdaQueryWrapper = new LambdaQueryWrapper<>();
            termLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId, roomId);
            roomLeaseTermService.remove(termLambdaQueryWrapper);
        }

        // 插入上面删除的新的信息
        // 7.插入图片
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if (graphVoList != null && !graphVoList.isEmpty()) {
            ArrayList<GraphInfo> graphInfos = new ArrayList<>();
            graphVoList.forEach(graphVo -> {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfos.add(graphInfo);
            });
            graphInfoService.saveBatch(graphInfos);
        }

        // 8.属性信息
        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (attrValueIds != null && !attrValueIds.isEmpty()) {
            ArrayList<RoomAttrValue> roomAttrValues = new ArrayList<>();
            attrValueIds.forEach(attrValueId -> {
                RoomAttrValue roomAttrValue = new RoomAttrValue();
                roomAttrValue.setRoomId(roomSubmitVo.getId());
                roomAttrValue.setAttrValueId(attrValueId);
                roomAttrValues.add(roomAttrValue);
            });
            roomAttrValueService.saveBatch(roomAttrValues);
        }

        // 9.插入配套
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (facilityInfoIds != null && !facilityInfoIds.isEmpty()) {
            ArrayList<RoomFacility> roomFacilities = new ArrayList<>();
            facilityInfoIds.forEach(facilityInfoId -> {
                RoomFacility roomFacility = new RoomFacility();
                roomFacility.setRoomId(roomSubmitVo.getId());
                roomFacility.setFacilityId(facilityInfoId);
                roomFacilities.add(roomFacility);
            });
            roomFacilityService.saveBatch(roomFacilities);
        }

        // 10.插入标签
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (labelInfoIds != null && !labelInfoIds.isEmpty()) {
            ArrayList<RoomLabel> roomLabels = new ArrayList<>();
            labelInfoIds.forEach(labelInfoId -> {
                RoomLabel roomLabel = new RoomLabel();
                roomLabel.setRoomId(roomSubmitVo.getId());
                roomLabel.setLabelId(labelInfoId);
                roomLabels.add(roomLabel);
            });
            roomLabelService.saveBatch(roomLabels);
        }

        // 11.插入支付方式
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (paymentTypeIds != null && !paymentTypeIds.isEmpty()) {
            ArrayList<RoomPaymentType> roomPaymentTypes = new ArrayList<>();
            paymentTypeIds.forEach(paymentTypeId -> {
                RoomPaymentType roomPaymentType = new RoomPaymentType();
                roomPaymentType.setRoomId(roomSubmitVo.getId());
                roomPaymentType.setPaymentTypeId(paymentTypeId);
                roomPaymentTypes.add(roomPaymentType);
            });
            roomPaymentTypeService.saveBatch(roomPaymentTypes);
        }

        // 12.插入可选租期
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (leaseTermIds != null && !leaseTermIds.isEmpty()) {
            ArrayList<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
            leaseTermIds.forEach(leaseTermId -> {
                RoomLeaseTerm roomLeaseTerm = new RoomLeaseTerm();
                roomLeaseTerm.setRoomId(roomSubmitVo.getId());
                roomLeaseTerm.setLeaseTermId(leaseTermId);
                roomLeaseTerms.add(roomLeaseTerm);
            });
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }
    }

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {

        // 根据id查询房间详细信息
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        BeanUtils.copyProperties(roomInfo, roomDetailVo);

        // 1.根据roomId查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());

        // 2.根据roomId和所属对象类型查询图片信息
        List<GraphVo> graphVos = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);

        // 3.根据roomId查询属性信息
        List<AttrValueVo> attrValueVos = attrValueMapper.selectListByRoomId(id);

        // 4.根据roomId查询配套信息
        List<FacilityInfo> facilityInfos = facilityInfoMapper.selectListByRoomId(id);

        // 5.查询标签信息
        List<LabelInfo> labelInfos = labelInfoMapper.selectListByRoomId(id);

        // 6.查询支付方式
        List<PaymentType> paymentTypes = paymentTypeMapper.selectListByRoomId(id);

        // 7.查询可选租期
        List<LeaseTerm> leaseTerms = leaseTermMapper.selectListByRoomId(id);

        // 封装以上信息并返回
        roomDetailVo.setApartmentInfo(apartmentInfo);
        roomDetailVo.setGraphVoList(graphVos);
        roomDetailVo.setAttrValueVoList(attrValueVos);
        roomDetailVo.setFacilityInfoList(facilityInfos);
        roomDetailVo.setLabelInfoList(labelInfos);
        roomDetailVo.setPaymentTypeList(paymentTypes);
        roomDetailVo.setLeaseTermList(leaseTerms);
        return roomDetailVo;
    }
}
