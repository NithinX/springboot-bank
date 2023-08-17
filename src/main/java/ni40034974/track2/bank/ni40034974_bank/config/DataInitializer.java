package ni40034974.track2.bank.ni40034974_bank.config;

import ni40034974.track2.bank.ni40034974_bank.account.Account;
import ni40034974.track2.bank.ni40034974_bank.address.Address;
import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import ni40034974.track2.bank.ni40034974_bank.account.AccountRepository;
import ni40034974.track2.bank.ni40034974_bank.customer.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public DataInitializer(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create customers
        Customer customer1 = new Customer(
                "Nithin",
                "Xavier",
                LocalDate.of(1998, 11, 22),
                "nithin.xavier.336@gmail.com",
                "9605309936",
                new Address("Puthuppally","Kottayam", "kerala", "686021")
                );
        customerRepository.save(customer1);

        Customer customer2 = new Customer(
                "Amal",
                "Alex",
                LocalDate.of(2001, 11, 24),
                "amal.alex@gmail.com",
                "9656001120",
                new Address("Changanacherry","Kottayam", "Kerala", "686031")
                );
        customerRepository.save(customer2);

        Customer customer3 = new Customer(
                "Kripa",
                "Binoy",
                LocalDate.of(2001, 8, 30),
                "kripabinoy@gmail.com",
                "9497087904",
                new Address("Athani","Kochi", "Kerala", "686551")
        );
        customerRepository.save(customer3);

        // Create accounts
        Account account1 = new Account(customer1, "savings", new BigDecimal("10000"));
        accountRepository.save(account1);

        Account account2 = new Account(customer1, "current", new BigDecimal("500"));
        accountRepository.save(account2);

        Account account3 = new Account(customer2, "savings", new BigDecimal("2000"));
        accountRepository.save(account3);

        Account account4 = new Account(customer2, "current", new BigDecimal("1500"));
        accountRepository.save(account4);

        Account account5 = new Account(customer3, "savings", new BigDecimal("25000"));
        accountRepository.save(account5);
    }
}
