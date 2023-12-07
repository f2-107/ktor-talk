package codecentric.de

import codecentric.de.app.web.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/api/v1") }
    }

    configureRouting()
}
