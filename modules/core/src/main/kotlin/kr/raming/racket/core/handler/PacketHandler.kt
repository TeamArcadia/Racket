package kr.raming.racket.core.handler

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.packet.Packet

interface PacketHandler {
	fun handle(ctx: ChannelHandlerContext, packet: Packet)
}