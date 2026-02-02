package com.greatxcf.lease.common.aliyunoss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.greatxcf.lease.common.aliyunoss")
//@EnableConfigurationProperties(ALiYunOssProperties.class)
public class ALiYunOssConfiguration {

    @Autowired
    private ALiYunOssProperties properties;

    // 创建并配置阿里云 OSS 客户端 Bean
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret()
        );
    }
}
