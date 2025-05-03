package kr.raming.racket.racket.packet

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class RacketPacketEncoder : MessageToByteEncoder<RacketPacketWrapper>() {
	override fun encode(ctx: ChannelHandlerContext, msg: RacketPacketWrapper, out: ByteBuf) {
		val channelBytes = msg.channel.toByteArray(Charsets.UTF_8)
		out.writeInt(channelBytes.size)
		out.writeBytes(channelBytes)
		out.writeBytes(msg.buf)
	}
}
