package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf

interface RacketPacketCodec<T : RacketPacket> {
	fun encode(packet: T, buf: ByteBuf)
	fun decode(buf: ByteBuf): T
}