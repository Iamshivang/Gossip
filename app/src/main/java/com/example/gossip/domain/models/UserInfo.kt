package com.example.gossip.domain.models

data class UserInfo(
    var USERNAME: String? = "",
    var BIO: String? = "",
    var DETAILS_ADDED: Boolean? = false,
    var EMAIL: String? = "",
    var DP_URL: String? = ""
)

