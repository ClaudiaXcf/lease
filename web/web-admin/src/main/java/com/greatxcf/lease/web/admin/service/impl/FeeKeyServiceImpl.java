package com.greatxcf.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.greatxcf.lease.model.entity.FeeKey;
import com.greatxcf.lease.web.admin.mapper.AttrKeyMapper;
import com.greatxcf.lease.web.admin.mapper.FeeKeyMapper;
import com.greatxcf.lease.web.admin.service.FeeKeyService;
import com.greatxcf.lease.web.admin.vo.fee.FeeKeyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liubo
* @description 针对表【fee_key(杂项费用名称表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class FeeKeyServiceImpl extends ServiceImpl<FeeKeyMapper, FeeKey>
    implements FeeKeyService {

    @Autowired
    private FeeKeyMapper feeKeyMapper;

    @Override
    public List<FeeKeyVo> feeInfoList() {
        FeeKeyVo feeKeyVo = new FeeKeyVo();
        return feeKeyMapper.feeInfoList();
    }
}




