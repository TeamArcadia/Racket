package kr.raming.racket.racket.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import kr.raming.racket.racket.packet.RacketPacketHandler
import kr.raming.racket.racket.packet.RacketPacketWrapper
import kr.raming.racket.racket.packet.readChannelId

class RacketServer(private val port: Int) {
	private val bossGroup = NioEventLoopGroup(1)
	private val workerGroup = NioEventLoopGroup()

	fun start() {
		val bootstrap = ServerBootstrap()
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					val pipeline = ch.pipeline()

					// 패킷 읽고 쓰는 로직 (여기선 ByteBuf을 직접 다룸)
					pipeline.addLast(object : SimpleChannelInboundHandler<ByteBuf>() {
						override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf) {
							val wrapper = RacketPacketWrapper(readChannelId(msg), msg, ctx.channel())
							RacketPacketHandler.handleIncoming(wrapper)
						}
					})
				}
			})

		bootstrap.bind(port).sync()
		println("RacketServer started on port $port")
	}

	fun shutdown() {
		bossGroup.shutdownGracefully()
		workerGroup.shutdownGracefully()
	}
}
