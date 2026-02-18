package com.greatxcf.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.greatxcf.lease.model.entity.*;
import com.greatxcf.lease.web.admin.mapper.*;
import com.greatxcf.lease.web.admin.service.LeaseAgreementService;
import com.greatxcf.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.greatxcf.lease.web.admin.vo.agreement.AgreementVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Override
    public IPage<AgreementVo> pageItem(Page<AgreementVo> page, AgreementQueryVo queryVo) {

        IPage<AgreementVo> result = leaseAgreementMapper.pageItem(page, queryVo);
        return result;
    }

    @Override
    public AgreementVo getAgreementVoById(Long id) {

        // 1.查询租约信息
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);

        // 2.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        // 3.查询房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        // 4.查询租期信息
        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        // 5.查询支付方式信息
        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        // 6.封装数据
        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(leaseAgreement, agreementVo);
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setLeaseTerm(leaseTerm);
        agreementVo.setPaymentType(paymentType);
        return agreementVo;
    }
}
