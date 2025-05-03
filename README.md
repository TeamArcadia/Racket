# Racket

**Racket**은 Minecraft **모드(Fabric)**와 **플러그인(Bukkit/Spigot)** 간의 통신을 위한  
**Plugin Message 기반 Netty 패킷 송수신 라이브러리**입니다.

이 라이브러리는 Minecraft 클라이언트-서버 간 커스텀 데이터를 효율적으로 교환할 수 있도록 설계되었으며,  
`ByteBuf` 기반으로 직렬화/역직렬화 구조를 직접 제어할 수 있도록 지원합니다.

---

## ✨ 특징

- 🧱 **Netty ByteBuf 기반 커스텀 패킷 직렬화**
- 🧩 **Fabric API, Mixin 없이 작동 가능**
- 🔌 **Plugin Message 기반 통신 전용**
- 📦 **멀티 모듈 구조 (`core` / `fabric` / `bukkit`)**
- 🧪 **경량 통신 구조 - 단순 의존성만으로 사용 가능**

---

## 📁 모듈 구조

| 모듈            | 설명                                                      |
|-----------------|-----------------------------------------------------------|
| `racket-core`   | 공통 패킷 정의, 채널 ID, 직렬화 유틸, 핸들러 관리 등 핵심 로직 |
| `racket-fabric` | Fabric 환경에서 `CustomPayloadC2SPacket` 전송 유틸리티 제공  |
| `racket-bukkit` | Bukkit 환경에서 Plugin Message 수신/송신 헬퍼 제공         |

---

## 🔧 의존성 설정

<details>
<summary><code>racket-core</code></summary>

```kotlin
dependencies {
    implementation("io.netty:netty-buffer:4.1.109.Final")
}
```

</details>

<details>
<summary><code>racket-fabric</code></summary>

```kotlin
dependencies {
    modImplementation("net.fabricmc:fabric-loader:0.14.22")
    implementation(project(":racket-core"))
}
```

</details>

<details>
<summary><code>racket-bukkit</code></summary>

```kotlin
dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation(project(":racket-core"))
}
```

</details>

---

## 🚀 사용 예시

### 1. 패킷 정의 (공통)

```kotlin
data class ChatMessagePacket(val message: String) : RacketPacket {
    override fun write(buf: ByteBuf) {
        ByteBufIO.writeString(buf, message)
    }

    companion object : RacketPacketCodec<ChatMessagePacket> {
        override fun read(buf: ByteBuf): ChatMessagePacket {
            return ChatMessagePacket(ByteBufIO.readString(buf))
        }
    }
}
```

---

### 2. Fabric 모드에서 패킷 전송

```kotlin
// Fabric 환경에서 서버로 패킷 전송
RacketFabricSender.sendToServer("racket:chat", ChatMessagePacket("Hello from client!"))
```

---

### 3. Bukkit 플러그인에서 패킷 수신

```kotlin
class MyListener : PluginMessageListener {
  override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
    if (channel != "racket:chat") return

    val buf = Unpooled.wrappedBuffer(message)
    val packet = ChatMessagePacket.read(buf)

    Bukkit.getLogger().info("받은 메시지: ${packet.message}")
  }
}
```
> Bukkit에서는 server.messenger.registerIncomingPluginChannel(...) 로 리스너를 등록하세요.

---

## ⚠️ 주의사항
* Racket은 독립적인 모드 또는 플러그인이 아닙니다. 모드 또는 플러그인 프로젝트에 라이브러리로 포함시켜 사용하세요.
* 전송/수신 모두 같은 채널 ID를 사용할 것 (ex: "racket:chat")