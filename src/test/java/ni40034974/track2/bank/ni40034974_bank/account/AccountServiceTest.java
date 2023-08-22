package ni40034974.track2.bank.ni40034974_bank.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import ni40034974.track2.bank.ni40034974_bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void testCreateAccount() {
        AccountService accountService = new AccountService(accountRepository);
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setAccountType("savings");
        newAccount.setBalance(BigDecimal.ZERO);

        when(accountRepository.findByCustomerAndAccountType(customer, "savings")).thenReturn(new ArrayList<>());
        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        Account result = accountService.createAccount(customer, "savings");

        assertEquals(customer, result.getCustomer());
        assertEquals("savings", result.getAccountType());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    void testCreateAccountExistingAccount() {
        AccountService accountService = new AccountService(accountRepository);
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        List<Account> existingAccounts = new ArrayList<>();
        existingAccounts.add(new Account());

        when(accountRepository.findByCustomerAndAccountType(customer, "savings")).thenReturn(existingAccounts);

        assertThrows(ResponseStatusException.class, () -> accountService.createAccount(customer, "savings"));
    }

    @Test
    void testDeposit() {
        AccountService accountService = new AccountService(accountRepository);
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.deposit(1L, new BigDecimal("100"));

        assertEquals(new BigDecimal("100"), result.getBalance());
    }

    @Test
    void testWithdrawSufficientFunds() {
        AccountService accountService = new AccountService(accountRepository);
        Account account = new Account();
        account.setBalance(new BigDecimal("200"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.withdraw(1L, new BigDecimal("100"));

        assertEquals(new BigDecimal("100"), result.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        AccountService accountService = new AccountService(accountRepository);
        Account account = new Account();
        account.setBalance(new BigDecimal("50"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(1L, new BigDecimal("100")));
    }

    @Test
    void testTransferFunds() {
        AccountService accountService = new AccountService(accountRepository);
        Account fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("200"));

        Account toAccount = new Account();
        toAccount.setBalance(new BigDecimal("100"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(fromAccount, toAccount);

        accountService.transferFunds(1L, 2L, new BigDecimal("100"));

        assertEquals(new BigDecimal("100"), fromAccount.getBalance());
        assertEquals(new BigDecimal("200"), toAccount.getBalance());
    }

    @Test
    void testTransferFundsInsufficientFunds() {
        AccountService accountService = new AccountService(accountRepository);
        Account fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("50"));

        Account toAccount = new Account();
        toAccount.setBalance(new BigDecimal("100"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(ResponseStatusException.class, () -> accountService.transferFunds(1L, 2L, new BigDecimal("100")));
    }
}
