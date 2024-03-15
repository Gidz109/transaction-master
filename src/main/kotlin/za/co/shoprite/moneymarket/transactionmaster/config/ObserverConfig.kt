package za.co.shoprite.moneymarket.transactionmaster.config

import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ObserverConfig {

    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry): ObservedAspect    {
        return ObservedAspect(observationRegistry)
    }

}