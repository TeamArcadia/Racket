package kr.raming.racket.racket.client

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.util.concurrent.DefaultThreadFactory
import kr.raming.racket.racket.packet.*

class RacketClient(
	private val host: String,
	private val port: Int
) {

	private lateinit var channel: Channel

	fun start() {
		val threadFactory = DefaultThreadFactory("racket-client", true)
		val group = NioEventLoopGroup(0, threadFactory)

		val bootstrap = Bootstrap()
			.group(group)
			.channel(NioSocketChannel::class.java)
			.handler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					val pipeline = ch.pipeline()

					pipeline.addLast("frameDecoder", LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4))
					pipeline.addLast("framePrepender", LengthFieldPrepender(4))

					pipeline.addLast("packetDecoder", RacketPacketDecoder())
					pipeline.addLast("packetEncoder", RacketPacketEncoder())

					pipeline.addLast("handler", object : SimpleChannelInboundHandler<RacketPacketWrapper>() {
						override fun channelRead0(ctx: ChannelHandlerContext, msg: RacketPacketWrapper) {
							RacketPacketHandler.handleIncoming(msg.channel, msg.buf, ctx.channel())
						}
					})
				}
			})

		val future = bootstrap.connect(host, port).sync()
		channel = future.channel()
	}

	fun sendPacket(channelName: String, packet: RacketPacket) {
		val buf = Unpooled.buffer()
		if (!RacketPacketHandler.encode(channelName, packet, buf)) {
			throw IllegalArgumentException("No codec registered for $channelName")
		}

		val wrapper = RacketPacketWrapper(channelName, buf)
		channel.writeAndFlush(wrapper)
	}
}
