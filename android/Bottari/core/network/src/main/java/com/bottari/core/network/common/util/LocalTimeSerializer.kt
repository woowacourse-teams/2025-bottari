package com.bottari.data.common.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

object LocalTimeSerializer : KSerializer<LocalTime> {
    private const val SERIAL_NAME = "LocalTime"
    override val descriptor = PrimitiveSerialDescriptor(SERIAL_NAME, PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: LocalTime,
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): LocalTime = LocalTime.parse(decoder.decodeString())
}
