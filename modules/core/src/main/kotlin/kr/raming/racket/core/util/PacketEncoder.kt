package kr.raming.racket.core.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.core.registry.PacketRegistry

class PacketEncoder : MessageToByteEncoder<Packet>() {
	override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
		val id = PacketRegistry.getId(msg)
		out.writeInt(id)
		msg.write(out)
	}
}