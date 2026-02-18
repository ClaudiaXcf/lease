package com.greatxcf.lease.web.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.greatxcf.lease.model.entity.LeaseAgreement;
import com.greatxcf.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.greatxcf.lease.web.admin.vo.agreement.AgreementVo;

/**
* @author liubo
* @description 针对表【lease_agreement(租约信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface LeaseAgreementService extends IService<LeaseAgreement> {

    IPage<AgreementVo> pageItem(Page<AgreementVo> page, AgreementQueryVo queryVo);

    AgreementVo getAgreementVoById(Long id);

}
