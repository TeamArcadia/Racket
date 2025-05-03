package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf

data class RacketPacketWrapper(val channel: String, val buf: ByteBuf, val sender: Any?)
