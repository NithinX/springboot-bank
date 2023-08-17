package ni40034974.track2.bank.ni40034974_bank.account;

import ni40034974.track2.bank.ni40034974_bank.customer.Customer;

import ni40034974.track2.bank.ni40034974_bank.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Account> createAccount(
            @PathVariable Long customerId,
            @RequestParam String accountType) {
        Customer customer = customerService.getCustomerById(customerId);
        Account account = accountService.createAccount(customer, accountType);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long accountId,
            @RequestParam BigDecimal amount) {
        Account account = accountService.deposit(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long accountId,
            @RequestParam BigDecimal amount) {
        Account account = accountService.withdraw(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        List<Account> accounts = accountService.getAccountsByCustomer(customer);
        if(accounts.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(accounts);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount) {
        accountService.transferFunds(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok("Funds transferred successfully");
    }

}
