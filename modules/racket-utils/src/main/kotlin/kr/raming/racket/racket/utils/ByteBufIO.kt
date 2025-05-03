package kr.raming.racket.racket.utils

import io.netty.buffer.ByteBuf

object ByteBufIO {
	fun writeString(buf: ByteBuf, value: String) {
		buf.writeInt(value.length)
		buf.writeBytes(value.toByteArray())
	}

	fun readString(buf: ByteBuf): String {
		val length = buf.readInt()
		val byteArray = ByteArray(length)
		buf.readBytes(byteArray)
		return String(byteArray)
	}
}