# Racket

---

## ✨ 특징

- 🛠️ **유연한 패킷 처리 시스템**
    - Netty ByteBuf 기반의 효율적인 데이터 직렬화/역직렬화
    - 커스텀 패킷 정의와 핸들링이 용이한 구조

- 🔄 **양방향 통신 지원**
    - 클라이언트-서버 간 원활한 데이터 교환
    - 비동기 메시지 처리 지원

- 📦 **모듈식 아키텍처**
    - 핵심 로직과 플랫폼별 구현의 명확한 분리
    - 독립적으로 사용 가능한 모듈 구조
    - 최소한의 의존성으로 가볍고 효율적인 구현


- 🔌 **플러그 앤 플레이**
    - 간단한 설정으로 빠른 통합 가능
    - 상세한 예제와 직관적인 API 제공


---

## 📁 모듈 구조

| 모듈            | 설명                                                      |
|-----------------|-----------------------------------------------------------|
| `core`          | 공통 패킷 정의, 채널 ID, 직렬화 유틸, 핸들러 관리 등 핵심 로직 |
| `client`        | Minecraft 클라이언트 측 통신 구현 |
| `server`        | Minecraft 서버 측 통신 구현 |
| `example`       | 라이브러리 사용 예시 프로젝트 |

---

## 🔧 의존성 설정

<details>
<summary><code>core</code></summary>

```kotlin
dependencies {
    implementation("io.netty:netty-buffer:4.1.109.Final")
}
```

</details>

<details>
<summary><code>client</code></summary>

```kotlin
dependencies {
    implementation(project(":core"))
    modImplementation("net.fabricmc:fabric-loader:0.14.22")
}
```

</details>

<details>
<summary><code>server</code></summary>

```kotlin
dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation(project(":racket-core"))
}
```

</details>

---

## 🚀 사용 예시

### 1. 패킷 정의

```kotlin
class EchoPacket(var message: String = "") : Packet {
    override fun write(buf: ByteBuf) {
        val bytes = message.toByteArray(Charsets.UTF_8)
        buf.writeInt(bytes.size)
        buf.writeBytes(bytes)
    }
    override fun read(buf: ByteBuf) {
        val length = buf.readInt()
        val bytes = ByteArray(length)
        buf.readBytes(bytes)
        message = bytes.toString(Charsets.UTF_8)
    }
}
```

---

### 2. 클라이언트 측 구현

```kotlin
class EchoClientHandler : ClientPacketHandler {
    override fun handle(ctx: ChannelHandlerContext, packet: Packet) {
        if (packet is EchoPacket) {
            println("클라이언트 수신: ${packet.message}")
        }
    }
}

val client = NettyClient(EchoClientHandler())
client.connect("localhost", 25565)
client.send(EchoPacket("안녕하세요!"))
```

---

### 3. 서버 측 구현

```kotlin
class EchoServerHandler : ServerPacketHandler {
    override fun handle(ctx: ChannelHandlerContext, packet: Packet) { 
        if (packet is EchoPacket) { 
            println("서버 수신: {packet.message}")
        }
    // 에코 응답 전송 ctx.writeAndFlush(EchoPacket("서버 응답:{packet.message}")) } } }
    }
}
// 서버 설정 및 실행 val server = NettyServer(EchoServerHandler()) server.bind(25565)
```

### 4. 패킷 등록

```kotlin
// 프로그램 시작 시 반드시 패킷을 등록해야 합니다
PacketRegistry.register(1) { EchoPacket() }
```

---

## ⚠️ 주의사항
-# Racket은 독립적인 모드 또는 플러그인이 아닙니다. 모드 또는 플러그인 프로젝트에 라이브러리로 포함시켜 사용하세요.

1. **패킷 등록**
    - 서버와 클라이언트 모두 동일한 패킷 ID로 등록해야 합니다
    - 패킷 등록은 반드시 서버/클라이언트 시작 전에 완료되어야 합니다
    - 패킷 ID는 프로젝트 전체에서 고유해야 합니다

2. **직렬화/역직렬화**
    - ByteBuf의 읽기/쓰기 순서가 정확히 일치해야 합니다
    - 문자열 인코딩(예: UTF-8)은 양쪽이 동일해야 합니다
    - ByteBuf 메모리 누수 방지를 위해 적절한 release가 필요합니다

3. **네트워크 처리**
    - 서버와 클라이언트의 연결 상태를 항상 확인해야 합니다
    - 긴 작업은 별도의 스레드에서 처리하여 네트워크 스레드 블로킹을 피해야 합니다
    - 예외 상황(연결 끊김, 타임아웃 등)에 대한 적절한 처리가 필요합니다

4. **리소스 관리**
    - 사용이 끝난 후에는 반드시 서버와 클라이언트를 정상적으로 종료해야 합니다
    - 장시간 실행되는 애플리케이션의 경우 메모리 누수에 주의해야 합니다
