package ni40034974.track2.bank.ni40034974_bank.controller;

import ni40034974.track2.bank.ni40034974_bank.model.Account;
import ni40034974.track2.bank.ni40034974_bank.service.AccountService;
import ni40034974.track2.bank.ni40034974_bank.model.Customer;

import ni40034974.track2.bank.ni40034974_bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<String> transferFunds(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount) {
        accountService.transferFunds(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok("Funds transferred successfully");
    }

}
