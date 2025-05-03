package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf

interface RacketPacket {
	fun write(buf: ByteBuf)
	fun read(buf: ByteBuf)
}