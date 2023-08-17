package ni40034974.track2.bank.ni40034974_bank.account;

import ni40034974.track2.bank.ni40034974_bank.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByCustomer(Customer customer);
    List<Account> findByCustomerAndAccountType(Customer customer, String accountType);

}
