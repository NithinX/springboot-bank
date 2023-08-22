package ni40034974.track2.bank.ni40034974_bank.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ni40034974.track2.bank.ni40034974_bank.address.Address;
import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import ni40034974.track2.bank.ni40034974_bank.customer.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void testFindByCustomer() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        Account account1 = new Account(customer, "savings", BigDecimal.valueOf(1000));
        Account account2 = new Account(customer, "current", BigDecimal.valueOf(500));

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findByCustomer(customer);

        assertEquals(2, accounts.size());
    }

    @Test
    void testFindByCustomerAndAccountType() {
        Customer customer = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
        );
        customerRepository.save(customer);

        Account account1 = new Account(customer, "savings", BigDecimal.valueOf(1000));
        Account account2 = new Account(customer, "current", BigDecimal.valueOf(500));

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> savingsAccounts = accountRepository.findByCustomerAndAccountType(customer, "savings");
        List<Account> currentAccounts = accountRepository.findByCustomerAndAccountType(customer, "current");

        assertEquals(1, savingsAccounts.size());
        assertEquals(1, currentAccounts.size());
    }

    @Test
    void testFindByNonExistentCustomer() {
        Customer nonExistentCustomer = new Customer();
        nonExistentCustomer.setCustomerId(999L);

        List<Account> accounts = accountRepository.findByCustomer(nonExistentCustomer);

        assertEquals(0, accounts.size());
    }

    @Test
    void testFindById() {
        Customer customer = new Customer();

        Account account = new Account(customer, "savings", BigDecimal.valueOf(1000));
        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getAccountNum());

        assertTrue(foundAccount.isPresent());
        assertEquals(savedAccount.getAccountNum(), foundAccount.get().getAccountNum());
    }

    @Test
    void testFindNonExistentById() {
        Optional<Account> foundAccount = accountRepository.findById(999L);

        assertFalse(foundAccount.isPresent());
    }
}
