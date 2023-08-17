package ni40034974.track2.bank.ni40034974_bank.account;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ni40034974.track2.bank.ni40034974_bank.exception.InsufficientFundsException;
import ni40034974.track2.bank.ni40034974_bank.customer.Customer;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNum;
    @ManyToOne
    private Customer customer;
    private String accountType; // savings, current, etc.
    private BigDecimal balance=BigDecimal.ZERO;

    public Account(Customer customer, String accountType, BigDecimal balance) {
        this.customer = customer;
        this.accountType = accountType;
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

}
