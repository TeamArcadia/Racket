package kr.raming.racket.example

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.client.handler.ClientPacketHandler
import kr.raming.racket.core.packet.Packet

class EchoClientHandler : ClientPacketHandler {
	override fun onConnected(ctx: ChannelHandlerContext) {
		println("클라이언트: 서버에 연결됨")

		// 서버에 패킷 전송
		ctx.writeAndFlush(EchoPacket("안녕하세요, 서버!"))
	}

	override fun onDisconnected(ctx: ChannelHandlerContext) {
		println("클라이언트: 연결 종료됨")
	}

	override fun onReconnect(ctx: ChannelHandlerContext) {
		TODO("Not yet implemented")
	}

	override fun onError(ctx: ChannelHandlerContext, cause: Throwable) {
		cause.printStackTrace()
	}

	override fun handle(ctx: ChannelHandlerContext, packet: Packet) {
		if (packet is EchoPacket) {
			println("클라이언트 수신: ${packet.message}")
		}
	}
}