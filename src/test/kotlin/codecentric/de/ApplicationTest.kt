package codecentric.de

import codecentric.de.app.web.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/api/v1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello KTOR-World!", bodyAsText())
        }
    }
}
