package org.example.api

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.example.CurrencyPairSerializer
import org.example.OrderSideSerializer
import java.util.UUID

@Serializable(with = OrderSideSerializer::class)
enum class OrderSide {
    BUY, SELL;

    companion object {
        fun fromString(value: String): OrderSide {
            return when (value) {
                "buy" -> BUY
                "sell" -> SELL
                else -> throw IllegalArgumentException("Unknown OrderSide value: $value")
            }
        }
    }
}

@Serializable(with = CurrencyPairSerializer::class)
enum class CurrencyPair {
    BTCZAR,
    ETHZAR,
    ETHBTC,
    LTCBTC,
    LTCETH,
    ADAETH,
    XEMBTC,
    XEMETH,
    ADABTC,
    ANTBTC,
    ANTETH,
    BATBTC,
    BATETH,
    BNTBTC,
    BNTETH,
    CVCBTC,
    CVCETH,
    BCHBTC,
    BCHETH,
    DCRBTC,
    DGBBTC,
    DGBETH,
    DNTBTC,
    ETCBTC,
    ETCETH,
    EXPBTC,
    GAMEBTC,
    GNOBTC,
    GNOETH,
    GNTBTC,
    GNTETH,
    LSKBTC,
    MONABTC,
    OMGBTC,
    OMGETH,
    PAYBTC,
    PAYETH,
    PIVXBTC,
    QTUMBTC,
    QTUMETH,
    RCNBTC,
    RLCBTC,
    SCBTC,
    SCETH,
    SNTBTC,
    SNTETH,
    STORJBTC,
    WAVESBTC,
    WAVESETH,
    TRXBTC,
    TRXETH,
    TUSDBTC,
    TUSDETH,
    XLMBTC,
    XLMETH,
    XRPBTC,
    XRPETH,
    ZRXBTC,
    ZRXETH,
    VIBBTC,
    VIBETH,
    DOGEBTC,
    VTCBTC,
    KMDBTC,
    BSVBTC,
    BSVETH,
    XRPZAR,
    DAIBTC,
    EOSBTC,
    LINKBTC,
    VETBTC,
    XTZBTC,
    DAIETH,
    EOSETH,
    XTZETH,
    QNTBTC,
    XTPBTC,
    CROBTC,
    BTCUSDT,
    ETHUSDT,
    XRPUSDT,
    STMXBTC,
    STMXETH,
    REPV2BTC,
    REPV2ETH,
    CELOBTC,
    CELOETH,
    ALGOBTC,
    COMPBTC,
    COMPETH,
    DOTBTC,
    DOTETH,
    PAXBTC,
    USDCBTC,
    USDCETH,
    SALTBTC,
    SALTETH,
    RLCETH,
    SLRBTC,
    POLYETH,
    GRSBTC,
    GUPBTC,
    MLNBTC,
    NMRBTC,
    SWTBTC,
    FCTBTC,
    WINGSBTC,
    OCNBTC,
    OCNETH,
    POLYBTC,
    REPBTC,
    REPETH,
    DASHBTC,
    DASHETH,
    STORMBTC,
    STORMETH,
    ZECBTC,
    ZECETH,
    XMRBTC,
    XMRETH,
    GRINBTC,
    MATICBTC,
    MATICETH,
    AAVEBTC,
    AAVEETH,
    UNIBTC,
    UNIETH,
    ENJBTC,
    ENJETH,
    MANABTC,
    MANAETH,
    SOLZAR,
    BTCUSDC,
    USDCZAR,
    BNBZAR,
    SHIBZAR,
    AVAXZAR,
    ETHUSDC,
    BNBUSDC,
    XRPUSDC,
    SOLUSDC,
    AVAXUSDC,
    SHIBUSDC,
    ETHWZAR,
    BTCZMW,
    USDCZMW,
    FLRUSDC,
    USDTZAR,
    USDTZMW,
    USDTUSDC,
    BTCINR,
    BTCUSDCPERP,
    ETHUSDCPERP,
    BTCZARPERP,
    BTCUSDTPERP,
    ETHUSDTPERP,
    ENJUSDC,
    USDTZARPERP,
    BNTUSDC,
    DAIUSDC,
    DNTUSDC,
    GNTUSDC,
    QNTUSDC,
    RLCUSDC,
    SNTUSDC,
    ZRXUSDC,
    STORJUSDC,
    YFIUSDC,
    LDOUSDC,
    MKRUSDC,
    CRVUSDC,
    HFTUSDC,
    APEUSDC,
    BANDUSDC,
    FILUSDC,
    APTUSDC,
    GRTUSDC,
    SUIUSDC,
    SNXUSDC,
    OPUSDC,
    ARBUSDC,
    BONKUSDC,
    FETUSDC,
    ILVUSDC;

    companion object {
        fun fromString(value: String): CurrencyPair {
            return when (value.uppercase()) {
                "BTCZAR" -> BTCZAR
                "ETHZAR" -> ETHZAR
                "ETHBTC" -> ETHBTC
                "LTCBTC" -> LTCBTC
                "LTCETH" -> LTCETH
                "ADAETH" -> ADAETH
                "XEMBTC" -> XEMBTC
                "XEMETH" -> XEMETH
                "ADABTC" -> ADABTC
                "ANTBTC" -> ANTBTC
                "ANTETH" -> ANTETH
                "BATBTC" -> BATBTC
                "BATETH" -> BATETH
                "BNTBTC" -> BNTBTC
                "BNTETH" -> BNTETH
                "CVCBTC" -> CVCBTC
                "CVCETH" -> CVCETH
                "BCHBTC" -> BCHBTC
                "BCHETH" -> BCHETH
                "DCRBTC" -> DCRBTC
                "DGBBTC" -> DGBBTC
                "DGBETH" -> DGBETH
                "DNTBTC" -> DNTBTC
                "ETCBTC" -> ETCBTC
                "ETCETH" -> ETCETH
                "EXPBTC" -> EXPBTC
                "GAMEBTC" -> GAMEBTC
                "GNOBTC" -> GNOBTC
                "GNOETH" -> GNOETH
                "GNTBTC" -> GNTBTC
                "GNTETH" -> GNTETH
                "LSKBTC" -> LSKBTC
                "MONABTC" -> MONABTC
                "OMGBTC" -> OMGBTC
                "OMGETH" -> OMGETH
                "PAYBTC" -> PAYBTC
                "PAYETH" -> PAYETH
                "PIVXBTC" -> PIVXBTC
                "QTUMBTC" -> QTUMBTC
                "QTUMETH" -> QTUMETH
                "RCNBTC" -> RCNBTC
                "RLCBTC" -> RLCBTC
                "SCBTC" -> SCBTC
                "SCETH" -> SCETH
                "SNTBTC" -> SNTBTC
                "SNTETH" -> SNTETH
                "STORJBTC" -> STORJBTC
                "WAVESBTC" -> WAVESBTC
                "WAVESETH" -> WAVESETH
                "TRXBTC" -> TRXBTC
                "TRXETH" -> TRXETH
                "TUSDBTC" -> TUSDBTC
                "TUSDETH" -> TUSDETH
                "XLMBTC" -> XLMBTC
                "XLMETH" -> XLMETH
                "XRPBTC" -> XRPBTC
                "XRPETH" -> XRPETH
                "ZRXBTC" -> ZRXBTC
                "ZRXETH" -> ZRXETH
                "VIBBTC" -> VIBBTC
                "VIBETH" -> VIBETH
                "DOGEBTC" -> DOGEBTC
                "VTCBTC" -> VTCBTC
                "KMDBTC" -> KMDBTC
                "BSVBTC" -> BSVBTC
                "BSVETH" -> BSVETH
                "XRPZAR" -> XRPZAR
                "DAIBTC" -> DAIBTC
                "EOSBTC" -> EOSBTC
                "LINKBTC" -> LINKBTC
                "VETBTC" -> VETBTC
                "XTZBTC" -> XTZBTC
                "DAIETH" -> DAIETH
                "EOSETH" -> EOSETH
                "XTZETH" -> XTZETH
                "QNTBTC" -> QNTBTC
                "XTPBTC" -> XTPBTC
                "CROBTC" -> CROBTC
                "BTCUSDT" -> BTCUSDT
                "ETHUSDT" -> ETHUSDT
                "XRPUSDT" -> XRPUSDT
                "STMXBTC" -> STMXBTC
                "STMXETH" -> STMXETH
                "REPV2BTC" -> REPV2BTC
                "REPV2ETH" -> REPV2ETH
                "CELOBTC" -> CELOBTC
                "CELOETH" -> CELOETH
                "ALGOBTC" -> ALGOBTC
                "COMPBTC" -> COMPBTC
                "COMPETH" -> COMPETH
                "DOTBTC" -> DOTBTC
                "DOTETH" -> DOTETH
                "PAXBTC" -> PAXBTC
                "USDCBTC" -> USDCBTC
                "USDCETH" -> USDCETH
                "SALTBTC" -> SALTBTC
                "SALTETH" -> SALTETH
                "RLCETH" -> RLCETH
                "SLRBTC" -> SLRBTC
                "POLYETH" -> POLYETH
                "GRSBTC" -> GRSBTC
                "GUPBTC" -> GUPBTC
                "MLNBTC" -> MLNBTC
                "NMRBTC" -> NMRBTC
                "SWTBTC" -> SWTBTC
                "FCTBTC" -> FCTBTC
                "WINGSBTC" -> WINGSBTC
                "OCNBTC" -> OCNBTC
                "OCNETH" -> OCNETH
                "POLYBTC" -> POLYBTC
                "REPBTC" -> REPBTC
                "REPETH" -> REPETH
                "DASHBTC" -> DASHBTC
                "DASHETH" -> DASHETH
                "STORMBTC" -> STORMBTC
                "STORMETH" -> STORMETH
                "ZECBTC" -> ZECBTC
                "ZECETH" -> ZECETH
                "XMRBTC" -> XMRBTC
                "XMRETH" -> XMRETH
                "GRINBTC" -> GRINBTC
                "MATICBTC" -> MATICBTC
                "MATICETH" -> MATICETH
                "AAVEBTC" -> AAVEBTC
                "AAVEETH" -> AAVEETH
                "UNIBTC" -> UNIBTC
                "UNIETH" -> UNIETH
                "ENJBTC" -> ENJBTC
                "ENJETH" -> ENJETH
                "MANABTC" -> MANABTC
                "MANAETH" -> MANAETH
                "SOLZAR" -> SOLZAR
                "BTCUSDC" -> BTCUSDC
                "USDCZAR" -> USDCZAR
                "BNBZAR" -> BNBZAR
                "SHIBZAR" -> SHIBZAR
                "AVAXZAR" -> AVAXZAR
                "ETHUSDC" -> ETHUSDC
                "BNBUSDC" -> BNBUSDC
                "XRPUSDC" -> XRPUSDC
                "SOLUSDC" -> SOLUSDC
                "AVAXUSDC" -> AVAXUSDC
                "SHIBUSDC" -> SHIBUSDC
                "ETHWZAR" -> ETHWZAR
                "BTCZMW" -> BTCZMW
                "USDCZMW" -> USDCZMW
                "FLRUSDC" -> FLRUSDC
                "USDTZAR" -> USDTZAR
                "USDTZMW" -> USDTZMW
                "USDTUSDC" -> USDTUSDC
                "BTCINR" -> BTCINR
                "BTCUSDCPERP" -> BTCUSDCPERP
                "ETHUSDCPERP" -> ETHUSDCPERP
                "BTCZARPERP" -> BTCZARPERP
                "BTCUSDTPERP" -> BTCUSDTPERP
                "ETHUSDTPERP" -> ETHUSDTPERP
                "ENJUSDC" -> ENJUSDC
                "USDTZARPERP" -> USDTZARPERP
                "BNTUSDC" -> BNTUSDC
                "DAIUSDC" -> DAIUSDC
                "DNTUSDC" -> DNTUSDC
                "GNTUSDC" -> GNTUSDC
                "QNTUSDC" -> QNTUSDC
                "RLCUSDC" -> RLCUSDC
                "SNTUSDC" -> SNTUSDC
                "ZRXUSDC" -> ZRXUSDC
                "STORJUSDC" -> STORJUSDC
                "YFIUSDC" -> YFIUSDC
                "LDOUSDC" -> LDOUSDC
                "MKRUSDC" -> MKRUSDC
                "CRVUSDC" -> CRVUSDC
                "HFTUSDC" -> HFTUSDC
                "APEUSDC" -> APEUSDC
                "BANDUSDC" -> BANDUSDC
                "FILUSDC" -> FILUSDC
                "APTUSDC" -> APTUSDC
                "GRTUSDC" -> GRTUSDC
                "SUIUSDC" -> SUIUSDC
                "SNXUSDC" -> SNXUSDC
                "OPUSDC" -> OPUSDC
                "ARBUSDC" -> ARBUSDC
                "BONKUSDC" -> BONKUSDC
                "FETUSDC" -> FETUSDC
                "ILVUSDC" -> ILVUSDC
                else -> throw IllegalArgumentException("Unknown CurrencyPair value: $value")
            }
        }
    }
}

@Serializable
data class Order(
    val id: String = UUID.randomUUID().toString(),
    val side: OrderSide,
    val quantity: Int,
    val price: Double,
    val currencyPair: CurrencyPair,
): Comparable<Order> {
    override fun compareTo(other: Order): Int {
        return this.quantity.compareTo(other.quantity)
    }
}

@Serializable
data class OrderBook(
    val sequence: Long,
    val asks: List<Order>,
    val bids: List<Order>,
    val lastChange: Instant
)

@Serializable
data class TradeHistory()