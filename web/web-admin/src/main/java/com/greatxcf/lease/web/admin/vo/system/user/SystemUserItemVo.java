package com.greatxcf.lease.web.admin.vo.system.user;


import com.baomidou.mybatisplus.annotation.TableField;
import com.greatxcf.lease.model.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "后台管理系统用户基本信息实体")
public class SystemUserItemVo extends SystemUser {

    @Schema(description = "岗位名称")
    @TableField(value = "post_name")
    private String postName;

}
