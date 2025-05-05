package kr.raming.racket.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import kr.raming.racket.client.handler.ClientChannelHandler
import kr.raming.racket.client.handler.ClientPacketHandler
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.core.util.PacketDecoder
import kr.raming.racket.core.util.PacketEncoder

class NettyClient(private val host: String, private val port: Int, private val handler: ClientPacketHandler) {

	private lateinit var channel: Channel

	fun start() {
		val group = NioEventLoopGroup()
		val future = Bootstrap()
			.group(group)
			.channel(NioSocketChannel::class.java)
			.handler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline().apply {
						addLast(PacketDecoder())
						addLast(PacketEncoder())
						addLast(ClientChannelHandler(handler))
					}
				}
			}).connect(host, port).sync()

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
