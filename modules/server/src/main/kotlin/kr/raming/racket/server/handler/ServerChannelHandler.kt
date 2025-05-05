package kr.raming.racket.server.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kr.raming.racket.core.packet.Packet

class ServerChannelHandler(
	private val handler: ServerPacketHandler
) : SimpleChannelInboundHandler<Packet>(Packet::class.java) {

	override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
		handler.handle(ctx, msg)
	}

	override fun channelActive(ctx: ChannelHandlerContext) {
		handler.onConnect(ctx)
	}

	override fun channelInactive(ctx: ChannelHandlerContext) {
		handler.onDisconnect(ctx)
	}

	@Deprecated("Deprecated in Java")
	override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
		handler.onError(ctx, cause)
		ctx.close()
	}
}