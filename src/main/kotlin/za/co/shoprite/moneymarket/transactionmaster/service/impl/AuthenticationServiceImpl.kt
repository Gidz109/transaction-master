package za.co.shoprite.moneymarket.transactionmaster.service.impl


import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService

@Service
class AuthenticationServiceImpl: AuthenticationService {

    override fun authenticate(username: String, password: String): String {
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