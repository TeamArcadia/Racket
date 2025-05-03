package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf

object RacketPacketHandler {
	private val codecs = mutableMapOf<String, RacketPacketCodec<out RacketPacket>>()
	private val handlers = mutableMapOf<String, (sender: Any?, packet: RacketPacket) -> Unit>()

	fun <T : RacketPacket> registerPacket(
		channel: String,
		codec: RacketPacketCodec<T>,
		handler: (sender: Any?, packet: T) -> Unit
	) {
		codecs[channel] = codec
		handlers[channel] = { sender, packet -> handler(sender, packet as T) }
	}

	fun handleIncoming(wrapper: RacketPacketWrapper) {
		val channelId = readChannelId(wrapper.buf)

		val codec = PacketRegistry.getCodec(channelId)
			?: throw IllegalArgumentException("Unknown channel ID: $channelId")

		val packet = codec.decode(wrapper.buf)
		handlers[wrapper.channel]?.invoke(wrapper.sender, packet)
	}

	fun encode(channel: String, packet: RacketPacket, buf: ByteBuf): Boolean {
		val codec = codecs[channel] ?: return false
		@Suppress("UNCHECKED_CAST")
		(codec as RacketPacketCodec<RacketPacket>).encode(packet, buf)
		return true
	}
}
