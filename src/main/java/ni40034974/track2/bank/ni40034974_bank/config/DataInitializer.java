package ni40034974.track2.bank.ni40034974_bank.config;

import ni40034974.track2.bank.ni40034974_bank.model.Account;
import ni40034974.track2.bank.ni40034974_bank.model.Customer;
import ni40034974.track2.bank.ni40034974_bank.repository.AccountRepository;
import ni40034974.track2.bank.ni40034974_bank.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        Customer customer1 = new Customer("John", "Doe");
        customerRepository.save(customer1);

        Customer customer2 = new Customer("Jane", "Smith");
        customerRepository.save(customer2);

        // Create accounts
        Account account1 = new Account(customer1, "savings", new BigDecimal("1000"));
        accountRepository.save(account1);

        Account account2 = new Account(customer1, "current", new BigDecimal("500"));
        accountRepository.save(account2);

        Account account3 = new Account(customer2, "savings", new BigDecimal("2000"));
        accountRepository.save(account3);

        Account account4 = new Account(customer2, "current", new BigDecimal("1500"));
        accountRepository.save(account4);
    }
}
