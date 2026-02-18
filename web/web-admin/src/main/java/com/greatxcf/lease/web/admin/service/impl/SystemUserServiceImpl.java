package com.greatxcf.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.greatxcf.lease.model.entity.SystemUser;
import com.greatxcf.lease.web.admin.mapper.SystemUserMapper;
import com.greatxcf.lease.web.admin.service.SystemUserService;
import com.greatxcf.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.greatxcf.lease.web.admin.vo.system.user.SystemUserQueryVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
                implements SystemUserService {

        @Autowired
        private SystemUserMapper systemUserMapper;

        @Override
        public IPage<SystemUserItemVo> pageItem(Page<SystemUserItemVo> page, SystemUserQueryVo queryVo) {
                IPage<SystemUserItemVo> result = systemUserMapper.pageItem(page, queryVo);
                return result;
        }

        @Override
        public SystemUserItemVo getByUserId(Long id) {
                return systemUserMapper.getByUserId(id);
        }
}
