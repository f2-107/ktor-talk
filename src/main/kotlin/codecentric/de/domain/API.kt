package codecentric.de.domain

class API(customerRepository: CustomerRepository) {

    private val saveCustomerUseCase = SaveCustomer(customerRepository)
    private val findCustomerUseCase = FindCustomer(customerRepository)
    private val allCustomersUseCase = AllCustomers(customerRepository)

    suspend fun saveCustomer(customer: Customer) = saveCustomerUseCase(customer)
    suspend fun findCustomer(id: String) = findCustomerUseCase(id)
    suspend fun allCustomers() = allCustomersUseCase()
}
