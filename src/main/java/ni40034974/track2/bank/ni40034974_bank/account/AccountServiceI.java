package ni40034974.track2.bank.ni40034974_bank.account;

import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountServiceI {

    Account createAccount(Customer customer, String accountType);
    Account deposit(Long accountId, BigDecimal amount);
    Account withdraw(Long accountId, BigDecimal amount);
    Account getBalance(Long accountId);
    List<Account> getAccountsByCustomer(Customer customer);
    void transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
