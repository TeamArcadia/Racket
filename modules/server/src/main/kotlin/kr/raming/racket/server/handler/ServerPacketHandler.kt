package kr.raming.racket.server.handler

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.handler.PacketHandler
import kr.raming.racket.core.packet.Packet

interface ServerPacketHandler : PacketHandler {
	/** 클라이언트 연결 시 처리 */
	fun onConnect(ctx: ChannelHandlerContext)

	/** 클라이언트 연결 종료 시 처리 */
	fun onDisconnect(ctx: ChannelHandlerContext)

	/** 예외 발생 시 처리 */
	fun onError(ctx: ChannelHandlerContext, cause: Throwable)
}