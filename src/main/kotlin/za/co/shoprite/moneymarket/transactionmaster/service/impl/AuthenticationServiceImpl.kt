package za.co.shoprite.moneymarket.transactionmaster.service.impl


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AuthenticationEntity
import za.co.shoprite.moneymarket.transactionmaster.model.entity.SessionEntity
import za.co.shoprite.moneymarket.transactionmaster.repository.AuthenticationRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.SessionRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.UserRepository
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService
import java.time.LocalDateTime
import org.apache.commons.codec.binary.Base64 as ApacheBase64

@Service
class AuthenticationServiceImpl: AuthenticationService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    lateinit var sessionRepository: SessionRepository

    override fun authenticate(username: String, password: String): String {

        val userEntity = userRepository.findByUsername(username)
        val authenticationEntity = authenticationRepository.findByUserId(userEntity.id)

        val base64: ApacheBase64 = ApacheBase64()

        if(!String(base64.decode(authenticationEntity.password)).equals(password) || authenticationEntity.locked)    {
            if(authenticationEntity.retryCount >= 2)    {
                authenticationEntity.locked = true
            }
            authenticationEntity.retryCount++
            authenticationRepository.save(authenticationEntity)
            throw BadCredentialsException("Unable to authenticate user")
        }

        val session = SessionEntity()
        session.timestamp = LocalDateTime.now()
        session.authenticationId = authenticationEntity.id
        sessionRepository.save(session)

        return issueToken()

    }

    fun issueToken(): String    {
        val url = "http://localhost:8010/default/token"

        val response = RestTemplate().postForEntity(url, buildAuthenticationRequest(), Response::class.java)

        if (response.statusCode != HttpStatus.OK) {
            throw BadCredentialsException("Unable to authenticate user")
        };
        return response.body?.access_token ?: ""
    }

    fun buildAuthenticationRequest(): MultiValueMap<String, String>    {
        val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap();
        requestBody.add("scope", "paymentsdomain.all")
        requestBody.add("grant_type", "authorization_code")
        requestBody.add("code", "O4gqy650H6JI42RxB0Gf1sVi_vpPd4TN0ZMH2Ipn_RA")
        requestBody.add("client_id", "123456")
        return requestBody
    }
}

data class Response(val token_type: String, val id_token: String, val access_token: String, val refresh_token: String, val expires_in: Int)