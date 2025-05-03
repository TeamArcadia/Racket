package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf
import kotlin.reflect.KClass

object PacketRegistry {
	private val codecsById = mutableMapOf<String, RacketPacketCodec<out RacketPacket>>()
	private val idsByClass = mutableMapOf<KClass<out RacketPacket>, String>()

	fun <T : RacketPacket> register(channelId: String, clazz: KClass<T>, codec: RacketPacketCodec<T>) {
		codecsById[channelId] = codec
		idsByClass[clazz] = channelId
	}

	fun getCodec(channelId: String): RacketPacketCodec<out RacketPacket>? {
		return codecsById[channelId]
	}

	fun <T : RacketPacket> getChannelId(clazz: KClass<T>): String? {
		return idsByClass[clazz]
	}
}

fun readChannelId(buf: ByteBuf): String {
	val length = readVarInt(buf)  // 채널 ID의 길이를 읽음
	val bytes = ByteArray(length) // 채널 ID의 실제 값이 저장될 배열
	buf.readBytes(bytes)  // 채널 ID를 ByteBuf에서 읽어서 배열에 저장
	return String(bytes, Charsets.UTF_8)  // 바이트 배열을 문자열로 변환하여 반환
}

fun readVarInt(buf: ByteBuf): Int {
	var numRead = 0
	var result = 0
	var read: Byte
	do {
		if (numRead >= 5) throw RuntimeException("VarInt is too big")

		read = buf.readByte()
		val value = (read.toInt() and 0b01111111)
		result = result or (value shl (7 * numRead))

		numRead++
	} while ((read.toInt() and 0b10000000) != 0)

	return result
}
