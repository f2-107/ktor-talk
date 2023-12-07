package codecentric.de.domain

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
    fun findAll(): List<Customer>
    fun saveCustomer(customer: Customer)
    fun findById(id: String): Customer?
}

class InMemoryCustomerStorage : CustomerRepository {
    val customerStorage = hashMapOf<String, Customer>(
        Pair("12345", Customer(Id("12345"), Name("Paula"), Age(30)))
    )
    override fun findAll(): List<Customer> = customerStorage.values.toList()

    override fun saveCustomer(customer: Customer) = customer.let { customerStorage[customer.id!!.value] = it }

    override fun findById(id: String) = customerStorage[id]
}
