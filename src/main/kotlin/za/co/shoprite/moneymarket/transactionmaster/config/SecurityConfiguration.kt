package za.co.shoprite.moneymarket.transactionmaster.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeRequests {
                authorize("/transaction/master//**", authenticated)
                authorize("/transaction/authenticate", permitAll)
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter
                }
            }
            csrf {
                disable()
            }
            cors {
                disable()
            }

        }
        return http.build()
    }

}