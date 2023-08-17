package ni40034974.track2.bank.ni40034974_bank.customer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerServiceI {
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Long customerId, Customer updatedCustomer);
    public Customer getCustomerById(Long customerId);
    public List<Customer> getCustomers();
    public void deleteCustomer(Long customerId);

}
