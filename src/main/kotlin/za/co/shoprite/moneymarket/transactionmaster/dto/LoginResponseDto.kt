package za.co.shoprite.moneymarket.transactionmaster.dto

data class LoginResponseDto(val jwtToken: String) {
    constructor() : this("") // Empty constructor
}
