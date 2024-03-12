package za.co.shoprite.moneymarket.transactionmaster.service

interface AuthenticationService {
    fun authenticate(username: String, password: String): String
}