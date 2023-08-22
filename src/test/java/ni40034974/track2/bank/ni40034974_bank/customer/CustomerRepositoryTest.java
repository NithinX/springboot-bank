package ni40034974.track2.bank.ni40034974_bank.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ni40034974.track2.bank.ni40034974_bank.address.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testExistsByEmail() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByEmail("nithin.xavier.336@gmail.com"));
        assertFalse(customerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void testExistsByContact() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByContact("9605309936"));
        assertFalse(customerRepository.existsByContact("9876543210"));
    }

    @Test
    void testFindCustomerByEmail() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        assertTrue(customerRepository.findCustomerByEmail("nithin.xavier.336@gmail.com").isPresent());
        assertFalse(customerRepository.findCustomerByEmail("nonexistent@example.com").isPresent());
    }

    @Test
    void testFindCustomerByContact() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        assertTrue(customerRepository.findCustomerByContact("9605309936").isPresent());
        assertFalse(customerRepository.findCustomerByContact("9876543210").isPresent());
    }
}
