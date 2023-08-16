package ni40034974.track2.bank.ni40034974_bank.repository;

import ni40034974.track2.bank.ni40034974_bank.model.Account;
import ni40034974.track2.bank.ni40034974_bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByCustomer(Customer customer);
    List<Account> findByCustomerAndAccountType(Customer customer, String accountType);

}
