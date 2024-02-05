package com.binus.timezone.ui.zonawaktu.model

data class Timezone(
    val datetime: String,
    val image: String,
    val latlng: String,
    val timezone: String,
    val utc_datetime: String,
    val utc_offset: String
)