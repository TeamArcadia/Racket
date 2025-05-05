package kr.raming.racket.example

import kr.raming.racket.client.NettyClient
import kr.raming.racket.core.registry.PacketRegistry
import kr.raming.racket.server.NettyServer

fun registerPackets() {
	PacketRegistry.register(1) { EchoPacket() }
}

fun main() {
	registerPackets()

	// 서버 실행
	Thread {
		val server = NettyServer(port = 8080, handler = EchoServerHandler())
		server.start()
	}.start()

	// 약간 대기 후 클라이언트 실행
	Thread.sleep(1000)
	val client = NettyClient("localhost", 8080, EchoClientHandler())
	client.start()

	Thread.sleep(1000)
	client.send(EchoPacket("송신 테스트입니다."))
}