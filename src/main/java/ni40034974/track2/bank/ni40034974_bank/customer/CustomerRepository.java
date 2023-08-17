package ni40034974.track2.bank.ni40034974_bank.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT s FROM Customer s WHERE s.email = ?1")
    Optional<Customer> findCustomerByEmail(String email);

    @Query("SELECT s FROM Customer s WHERE s.contact = ?1")
    Optional<Customer> findCustomerByContact(String contact);

    Boolean existsByEmail(String email);
    Boolean existsByContact(String contact);


}
