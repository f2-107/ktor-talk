package codecentric.de.domain

import codecentric.de.domain.CustomerDatabaseSingleton.dbQuery
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@JvmInline
value class Id(val value: String)

@JvmInline
value class Name(val value: String)

@JvmInline
value class Age(val value: Long)

data class Customer(
    val id: Id?,
    val name: Name,
    val age: Age?,
)

interface CustomerRepository {
    suspend fun findAll(): List<Customer>
    suspend fun saveCustomer(customer: Customer)
    suspend fun findById(id: String): Customer?
}

object Customers : Table() {
    val id = varchar("id", 36)
    val name = varchar("name", 255)
    val age = long("age")

    override val primaryKey = PrimaryKey(id)
}

object CustomerDatabaseSingleton {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Customers)
        }

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}



class DatabasseCustomerStorage : CustomerRepository {
    private fun resultRowToCustomer(row: ResultRow) = Customer(
        id = Id(row[Customers.id]),
        name = Name(row[Customers.name]),
        age = Age(row[Customers.age]),
    )

    override suspend fun findAll(): List<Customer> = dbQuery { Customers.selectAll().map(::resultRowToCustomer) }

    override suspend fun saveCustomer(customer: Customer): Unit = dbQuery {
        val insertStatement = Customers.insert {
            it[id] = customer.id!!.value
            it[name] = customer.name.value
            it[age] = customer.age!!.value
        }

        insertStatement.resultedValues?.singleOrNull()
    }

    override suspend fun findById(id: String): Customer? =
        dbQuery { Customers.select(Customers.id eq id).map(::resultRowToCustomer).singleOrNull() }

}
