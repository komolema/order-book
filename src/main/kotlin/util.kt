package org.example

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object OrderSideSerializer: KSerializer<OrderSide> {
    override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("OrderSide", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OrderSide {
        return OrderSide.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: OrderSide) {
        encoder.encodeString(value.name.lowercase())
    }

}


object CurrencyPairSerializer : KSerializer<CurrencyPair> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CurrencyPair", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CurrencyPair {
        return CurrencyPair.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: CurrencyPair) {
        encoder.encodeString(value.name.lowercase())
    }
}