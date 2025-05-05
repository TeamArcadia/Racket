package kr.raming.racket.core.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import kr.raming.racket.core.registry.PacketRegistry

class PacketDecoder : ByteToMessageDecoder() {
	override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
		val id = `in`.readInt()
		val packet = PacketRegistry.create(id) ?: throw IllegalArgumentException("Unknown packet ID: $id")
		packet.read(`in`)
		out.add(packet)
	}
}