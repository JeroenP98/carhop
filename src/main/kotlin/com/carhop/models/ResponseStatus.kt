package com.carhop.models

import kotlinx.serialization.Serializable

//Class for creating responding to API requests
@Serializable
data class ResponseStatus(
    var status: String
)