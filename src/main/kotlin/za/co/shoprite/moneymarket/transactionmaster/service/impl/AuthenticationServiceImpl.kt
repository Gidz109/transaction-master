package za.co.shoprite.moneymarket.transactionmaster.service.impl


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import za.co.shoprite.moneymarket.transactionmaster.model.entity.SessionEntity
import za.co.shoprite.moneymarket.transactionmaster.repository.AuthenticationRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.SessionRepository
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService
import java.time.LocalDateTime

@Service
class AuthenticationServiceImpl: AuthenticationService {

    @Autowired
    lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    lateinit var sessionRepository: SessionRepository

    @Value("\${application.properties.keycloak.token.url}")
    lateinit var keycloakTokenUrl: String

    override fun authenticate(username: String, password: String): String {

        val authenticationEntity = authenticationRepository.findByUsername(username)
        var token = ""
        if(!authenticationEntity.locked)    {
            try {
                token = issueToken(username, password)
            } catch (bce: BadCredentialsException) {
                authenticationEntity.retryCount++
                if (authenticationEntity.retryCount >= 2)   {
                    authenticationEntity.locked = true
                }
                authenticationRepository.save(authenticationEntity)
                throw bce
            }
        }

        val session = SessionEntity()
        session.timestamp = LocalDateTime.now()
        session.authenticationId = authenticationEntity.id
        sessionRepository.save(session)

        return token

    }

    fun issueToken(username: String, password: String): String    {

        val response = RestTemplate().postForEntity(keycloakTokenUrl, buildAuthenticationRequest(username, password), Response::class.java)

        if (response.statusCode != HttpStatus.OK) {
            throw BadCredentialsException("Unable to authenticate user")
        }
        return response.body?.access_token ?: ""
    }

    fun buildAuthenticationRequest(username: String, password: String): MultiValueMap<String, String>    {
        val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestBody.add("username", username)
        requestBody.add("grant_type", "password")
        requestBody.add("password", password)
        requestBody.add("client_id", "transaction-master")
        return requestBody
    }
}

data class Response(val token_type: String, val access_token: String, val refresh_token: String, val expires_in: Int)