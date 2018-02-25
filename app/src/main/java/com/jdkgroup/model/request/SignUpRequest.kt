package com.jdkgroup.model.request

import java.util.*

class SignUpRequest {
    var username: String? = null
    var email: String? = null
    var country: String? = null
    var state: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var address: String? = null
    var profilepicture: String? = null
    var password: String? = null
    var gender: Int = 0
    var status: Int = 0

    constructor() {}

    constructor(username: String, email: String, country: String, state: String, mobile: String, pincode: String, address: String, profilepicture: String, password: String, gender: Int, status: Int) {
        this.username = username
        this.email = email
        this.password = password
        this.mobile = mobile
        this.gender = gender
        this.country = country
        this.state = state
        this.pincode = pincode
        this.address = address
        this.profilepicture = profilepicture
        this.status = status
    }

}