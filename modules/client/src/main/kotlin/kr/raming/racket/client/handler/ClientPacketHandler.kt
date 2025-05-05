package kr.raming.racket.client.handler

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.handler.PacketHandler
import kr.raming.racket.core.packet.Packet

interface ClientPacketHandler : PacketHandler {
	/** 서버 연결 성공 시 처리 */
	fun onConnected(ctx: ChannelHandlerContext)

	/** 서버 연결 종료 시 처리 */
	fun onDisconnected(ctx: ChannelHandlerContext)

	/** 재연결 시도 처리 */
	fun onReconnect(ctx: ChannelHandlerContext)

	/** 예외 발생 시 처리 */
	fun onError(ctx: ChannelHandlerContext, cause: Throwable)
}