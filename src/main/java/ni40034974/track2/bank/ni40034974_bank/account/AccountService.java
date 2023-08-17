package ni40034974.track2.bank.ni40034974_bank.account;


import ni40034974.track2.bank.ni40034974_bank.exception.AccountTypeExistsException;
import ni40034974.track2.bank.ni40034974_bank.exception.EntityNotFoundException;

import ni40034974.track2.bank.ni40034974_bank.exception.InsufficientFundsException;
import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Customer customer, String accountType) {
        //allow only current and savings account types
        if(!accountType.isEmpty() && !accountType.equals("savings") && !accountType.equals("current")){
            throw new IllegalStateException("Invalid Account type");
        }
        // Check if the customer already has an account of the given type
        List<Account> existingAccounts = accountRepository.findByCustomerAndAccountType(customer, accountType);
        if (!existingAccounts.isEmpty()) {
            throw new AccountTypeExistsException("Customer already has an account of type: " + accountType);
        }
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(accountType);
        return accountRepository.save(account);
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.deposit(amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByCustomer(Customer customer) {
        return accountRepository.findByCustomer(customer);
    }

    public void transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("From Account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new EntityNotFoundException("To Account not found"));

        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } else {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }
    }

}
