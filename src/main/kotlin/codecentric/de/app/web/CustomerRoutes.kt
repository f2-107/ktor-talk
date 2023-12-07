package codecentric.de.app.web

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        route("/api/v1") {
            get { call.respond("Hello KTOR-World!") }
        }
    }
}
