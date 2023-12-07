package codecentric.de.domain

import java.util.*

class FindCustomer(val customerRepository: CustomerRepository) {
    operator fun invoke( id: String): Customer? {
        return customerRepository.findById(id)
    }
}

class SaveCustomer(val customerRepository: CustomerRepository) {
    operator fun invoke(customer: Customer) {
        val customerToSave = when (customer.id) {
            null -> customer.copy(id = Id(UUID.randomUUID().toString()))
            else -> customer
        }

        customerRepository.saveCustomer(customerToSave)
    }
}

class AllCustomers(val customerRepository: CustomerRepository) {

    operator fun invoke(): List<Customer> {
        return customerRepository.findAll()
    }
}
