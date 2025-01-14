package com.dtp.core.spring;

import com.dtp.common.ApplicationContextHolder;
import com.dtp.common.constant.DynamicTpConst;
import com.dtp.common.properties.DtpProperties;
import com.dtp.common.timer.HashedWheelTimer;
import com.dtp.core.DtpRegistry;
import com.dtp.core.monitor.DtpEndpoint;
import com.dtp.core.monitor.DtpMonitor;
import com.dtp.core.support.DtpBannerPrinter;
import com.dtp.core.thread.NamedThreadFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Role;

import java.util.concurrent.TimeUnit;

/**
 * DtpBaseBeanConfiguration related
 *
 * @author yanhom
 * @since 1.0.0
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DtpProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DtpBaseBeanConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ApplicationContextHolder dtpApplicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @DependsOn({"dtpApplicationContextHolder"})
    public DtpPostProcessor dtpPostProcessor() {
        return new DtpPostProcessor();
    }

    @Bean
    public DtpRegistry dtpRegistry(DtpProperties dtpProperties) {
        return new DtpRegistry(dtpProperties);
    }

    @Bean
    public DtpMonitor dtpMonitor(DtpProperties dtpProperties) {
        return new DtpMonitor(dtpProperties);
    }

    @Bean
    @ConditionalOnAvailableEndpoint
    public DtpEndpoint dtpEndpoint() {
        return new DtpEndpoint();
    }

    @Bean
    @ConditionalOnProperty(name = DynamicTpConst.BANNER_ENABLED_PROP, matchIfMissing = true, havingValue = "true")
    public DtpBannerPrinter dtpBannerPrinter() {
        return new DtpBannerPrinter();
    }

    @Bean
    public HashedWheelTimer hashedWheelTimer() {
        return new HashedWheelTimer(new NamedThreadFactory("dtpRunnable-timeout", true), 10, TimeUnit.MILLISECONDS);
    }

}
