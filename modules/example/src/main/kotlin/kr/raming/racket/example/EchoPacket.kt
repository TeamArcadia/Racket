package kr.raming.racket.example

import io.netty.buffer.ByteBuf
import kr.raming.racket.core.packet.Packet

class EchoPacket(var message: String = "") : Packet {

	override fun write(buf: ByteBuf) {
		val bytes = message.toByteArray(Charsets.UTF_8)
		buf.writeInt(bytes.size)
		buf.writeBytes(bytes)
	}

	override fun read(buf: ByteBuf) {
		val length = buf.readInt()
		val bytes = ByteArray(length)
		buf.readBytes(bytes)
		message = String(bytes, Charsets.UTF_8)
	}
}
