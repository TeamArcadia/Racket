package kr.raming.racket.core.packet

import io.netty.buffer.ByteBuf

interface Packet {
	fun write(buf: ByteBuf)
	fun read(buf: ByteBuf)
}