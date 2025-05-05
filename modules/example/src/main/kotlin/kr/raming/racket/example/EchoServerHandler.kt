package kr.raming.racket.example

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.server.handler.ServerPacketHandler

class EchoServerHandler : ServerPacketHandler {

	override fun onConnect(ctx: ChannelHandlerContext) {
		println("서버: 클라이언트 접속")
	}

	override fun onDisconnect(ctx: ChannelHandlerContext) {
		println("서버: 클라이언트 접속 해제")
	}

	override fun onError(ctx: ChannelHandlerContext, cause: Throwable) {
		cause.printStackTrace()
	}

	override fun handle(ctx: ChannelHandlerContext, packet: Packet) {
		if (packet is EchoPacket) {
			println("서버 수신: ${packet.message}")

			// 받은 메시지를 그대로 클라이언트에 다시 전송
			ctx.writeAndFlush(EchoPacket("서버도 반가워요!"))
		}
	}
}
