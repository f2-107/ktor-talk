package codecentric.de

import codecentric.de.app.web.configureRouting
import codecentric.de.domain.API
import codecentric.de.domain.CustomerRepository
import codecentric.de.domain.InMemoryCustomerStorage
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

val appModule = module {
    single<CustomerRepository> { InMemoryCustomerStorage() }
    single { API(get()) }
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/api/v1") }
    }

    configureRouting(inject<API>().value)
}
