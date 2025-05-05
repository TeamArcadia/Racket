package kr.raming.racket.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.core.util.PacketDecoder
import kr.raming.racket.core.util.PacketEncoder
import kr.raming.racket.server.handler.ServerChannelHandler
import kr.raming.racket.server.handler.ServerPacketHandler

class NettyServer(private val port: Int, private val handler: ServerPacketHandler) {

	private lateinit var channel: Channel

	fun start() {
		val boss = NioEventLoopGroup()
		val worker = NioEventLoopGroup()

		val future = ServerBootstrap()
			.group(boss, worker)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline().apply {
						addLast(PacketDecoder())
						addLast(PacketEncoder())
						addLast(ServerChannelHandler(handler))
					}
				}
			}).bind(port).sync()

		channel = future.channel()
	}

	fun send(packet: Packet): Boolean {
		val ch = channel
		return if (ch != null && ch.isActive) {
			ch.writeAndFlush(packet)
			true
		} else {
			println("채널이 연결되지 않았거나 비활성 상태입니다.")
			false
		}
	}
}
