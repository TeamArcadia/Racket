package kr.raming.racket.core.registry

import kr.raming.racket.core.packet.Packet
import kotlin.reflect.KClass

object PacketRegistry {
	private val idToSupplier = mutableMapOf<Int, () -> Packet>()
	private val classToId = mutableMapOf<KClass<out Packet>, Int>()

	fun register(id: Int, supplier: () -> Packet) {
		idToSupplier[id] = supplier
		classToId[supplier().javaClass.kotlin] = id
	}

	fun create(id: Int): Packet? = idToSupplier[id]?.invoke()
	fun getId(packet: Packet): Int = classToId[packet::class] ?: error("Unknown packet: $packet")
}