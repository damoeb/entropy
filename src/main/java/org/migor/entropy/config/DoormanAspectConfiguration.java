package org.migor.entropy.config;

import org.migor.entropy.aop.request.DoormanAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class DoormanAspectConfiguration {

    @Bean
    public DoormanAspect floodControlAspect() {
        return new DoormanAspect();
    }
}
