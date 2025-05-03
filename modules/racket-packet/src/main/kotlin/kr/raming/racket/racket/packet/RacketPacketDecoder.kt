package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class RacketPacketDecoder : ByteToMessageDecoder() {
	override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any>) {
		if (input.readableBytes() < 4) return
		input.markReaderIndex()

		val channelLength = input.readInt()
		if (input.readableBytes() < channelLength) {
			input.resetReaderIndex()
			return
		}

		val channelBytes = ByteArray(channelLength)
		input.readBytes(channelBytes)
		val channel = String(channelBytes, Charsets.UTF_8)

		val payload = input.readBytes(input.readableBytes())
		out.add(RacketPacketWrapper(channel, payload))
	}
}
