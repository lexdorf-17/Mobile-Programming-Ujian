package com.binus.timezone.ui.zonawaktu.model

data class TimezoneResponse(
    val metadata: Metadata,
    val record: Record
)

data class Metadata(
    val collectionId: String,
    val createdAt: String,
    val id: String,
    val name: String,
    val `private`: Boolean
)

data class Record(
    val timezone: List<Timezone>
)