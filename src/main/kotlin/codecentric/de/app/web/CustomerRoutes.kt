package codecentric.de.app.web

import codecentric.de.domain.API
import codecentric.de.domain.Age
import codecentric.de.domain.Customer
import codecentric.de.domain.Id
import codecentric.de.domain.Name
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting(api: API) {

    install(ContentNegotiation) {
        json()
    }

    routing {
        authenticate("basic-auth") {
            route("/api/v1/customer") {

                get {
                    call.respond(api.allCustomers().map { CustomerDTO.fromDomain(it) })
                }

                get("/{id}") {
                    call.respond(
                        api.findCustomer(call.parameters["id"]!!)?.let { customer -> CustomerDTO.fromDomain(customer) }
                            ?: HttpStatusCode.NotFound)
                }

                put {
                    api.saveCustomer(call.receive<CustomerDTO>().toDomain())
                    call.respond(HttpStatusCode.NoContent)
                }
            }

        }
    }
}

@Serializable
data class CustomerDTO(val id: String?, val name: String, val age: Long?) {
    companion object
}

private fun CustomerDTO.toDomain(): Customer = Customer(id?.let { Id(it) }, Name(name), age?.let { Age(it) })

private fun CustomerDTO.Companion.fromDomain(customer: Customer) = CustomerDTO(
    customer.id?.value,
    customer.name.value,
    customer.age?.value
)
