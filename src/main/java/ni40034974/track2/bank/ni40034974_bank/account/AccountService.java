package ni40034974.track2.bank.ni40034974_bank.account;


import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements AccountServiceI {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Customer customer, String accountType) {
        //allow only current and savings account types
        if(!accountType.isEmpty() && !accountType.equals("savings") && !accountType.equals("current")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Account type");
        }
        // Check if the customer already has an account of the given type
        List<Account> existingAccounts = accountRepository.findByCustomerAndAccountType(customer, accountType);
        if (!existingAccounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer already has an account of type: " + accountType);
        }
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(accountType);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isPresent())
            accountRepository.deleteById(accountId);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No Account found with Id "+accountId);
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found with Id "+accountId));
        account.deposit(amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found with Id "+accountId));
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByCustomer(Customer customer) {
        return accountRepository.findByCustomer(customer);
    }

    public void transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"From Account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"To Account not found"));

        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient funds for transfer");
        }
    }

    public BigDecimal getBalance(Long accountId) {
        Account account =accountRepository.findById(accountId)
                .orElseThrow(()-> new  ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found with Id "+accountId));
        return account.getBalance();
    }
}
