package ni40034974.track2.bank.ni40034974_bank.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ni40034974.track2.bank.ni40034974_bank.account.Account;
import ni40034974.track2.bank.ni40034974_bank.account.AccountRepository;
import ni40034974.track2.bank.ni40034974_bank.account.AccountService;
import ni40034974.track2.bank.ni40034974_bank.address.Address;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CustomerServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void testCreateCustomer() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer newCustomer = new Customer();
        newCustomer.setEmail("test@example.com");
        newCustomer.setContact("1234567890");

        when(customerRepository.existsByEmail(newCustomer.getEmail())).thenReturn(false);
        when(customerRepository.existsByContact(newCustomer.getContact())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        Customer result = customerService.createCustomer(newCustomer);

        assertEquals("test@example.com", result.getEmail());
        assertEquals("1234567890", result.getContact());
    }

    @Test
    void testCreateCustomerExistingEmail() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setEmail("existing@example.com");

        when(customerRepository.existsByEmail(existingCustomer.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> customerService.createCustomer(existingCustomer));
    }

    @Test
    void testCreateCustomerExistingContact() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setContact("9876543210");

        when(customerRepository.existsByContact(existingCustomer.getContact())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> customerService.createCustomer(existingCustomer));
    }

    @Test
    void testUpdateCustomer() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setEmail("existing@example.com");
        existingCustomer.setContact("1234567890");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setEmail("updated@example.com");
        updatedCustomer.setContact("9876543210");

        when(customerRepository.findById(existingCustomer.getCustomerId())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findCustomerByEmail(updatedCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findCustomerByContact(updatedCustomer.getContact())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(existingCustomer.getCustomerId(), updatedCustomer);

        assertEquals("updated@example.com", result.getEmail());
        assertEquals("9876543210", result.getContact());
    }

    @Test
    void testUpdateCustomerExistingEmail() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setEmail("existingemail@example.com");

        Customer anotherCustomer = new Customer();
        anotherCustomer.setCustomerId(2L);
        anotherCustomer.setEmail("existing@example.com");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setEmail("existing@example.com"); // Trying to update with an existing email

        when(customerRepository.findById(existingCustomer.getCustomerId())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findCustomerByEmail(updatedCustomer.getEmail())).thenReturn(Optional.of(anotherCustomer));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                customerService.updateCustomer(existingCustomer.getCustomerId(), updatedCustomer));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email is already in use", exception.getReason());
    }

    @Test
    void testUpdateCustomerExistingContact() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setContact("9999999999");

        Customer anotherCustomer = new Customer();
        anotherCustomer.setCustomerId(2L);
        anotherCustomer.setContact("8888888888");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setContact("8888888888"); // Trying to update with an existing contact

        when(customerRepository.findById(existingCustomer.getCustomerId())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findCustomerByContact(updatedCustomer.getContact())).thenReturn(Optional.of(anotherCustomer));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                customerService.updateCustomer(existingCustomer.getCustomerId(), updatedCustomer));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Contact is already in use", exception.getReason());

    }

    @Test
    void testGetCustomerById() {
        CustomerService customerService = new CustomerService(customerRepository);

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);

        when(customerRepository.findById(existingCustomer.getCustomerId())).thenReturn(Optional.of(existingCustomer));

        Customer result = customerService.getCustomerById(existingCustomer.getCustomerId());

        assertEquals(existingCustomer, result);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        CustomerService customerService = new CustomerService(customerRepository);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void testGetCustomers() {
        CustomerService customerService = new CustomerService(customerRepository);

        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer());
        customerList.add(new Customer());

        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> result = customerService.getCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteCustomer() {
        Customer existingCustomer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally", "Kottayam", "kerala", "686021")
        );

        when(customerRepository.findById(existingCustomer.getCustomerId())).thenReturn(Optional.of(existingCustomer));

        Account account1 = new Account(existingCustomer, "savings", BigDecimal.valueOf(1000));
        Account account2 = new Account(existingCustomer, "current", BigDecimal.valueOf(500));

        List<Account> relatedAccounts = new ArrayList<>();
        relatedAccounts.add(account1);
        relatedAccounts.add(account2);

        when(accountRepository.findByCustomer(existingCustomer)).thenReturn(relatedAccounts);
        doNothing().when(accountRepository).deleteAll(relatedAccounts);
        doNothing().when(customerRepository).deleteById(existingCustomer.getCustomerId());

        customerService.deleteCustomer(existingCustomer.getCustomerId());

        verify(accountRepository, times(1)).deleteAll(relatedAccounts);
        verify(customerRepository, times(1)).deleteById(existingCustomer.getCustomerId());
    }

}
