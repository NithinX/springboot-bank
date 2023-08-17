package ni40034974.track2.bank.ni40034974_bank.customer;

import ni40034974.track2.bank.ni40034974_bank.account.Account;
import ni40034974.track2.bank.ni40034974_bank.account.AccountRepository;
import ni40034974.track2.bank.ni40034974_bank.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    @Autowired
    public void AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Customer createCustomer(Customer customer) {
        // Check if the email is already used
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalStateException("Email is already in use");
        }
        // Check if the contact number is already used
        if (customerRepository.existsByContact(customer.getContact())) {
            throw new IllegalStateException("Contact number is already in use");
        }
        return customerRepository.save(customer);
    }


    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id "+customerId+" does exist"));

        if(updatedCustomer.getEmail() != null && !updatedCustomer.getEmail().isEmpty() && !Objects.equals(updatedCustomer.getEmail(),existingCustomer.getEmail())) {
            Optional<Customer> customerOptional = customerRepository.findCustomerByEmail(updatedCustomer.getEmail());
            if(customerOptional.isPresent()){
                throw new IllegalStateException("Email is already in use");
            }
            existingCustomer.setEmail(updatedCustomer.getEmail());
        }

        if(updatedCustomer.getContact()!=null && !updatedCustomer.getContact().isEmpty() && !Objects.equals(updatedCustomer.getContact(),existingCustomer.getContact())){
            Optional<Customer> customerOptional = customerRepository.findCustomerByContact(updatedCustomer.getContact());
            if(customerOptional.isPresent()){
                throw new IllegalStateException("Contact is already in use");
            }
            existingCustomer.setContact(updatedCustomer.getContact());
        }

        if (updatedCustomer.getFirstName() != null) {
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
        }
        if (updatedCustomer.getLastName() != null) {
            existingCustomer.setLastName(updatedCustomer.getLastName());
        }
        if (updatedCustomer.getDob() != null) {
            existingCustomer.setDob(updatedCustomer.getDob());
        }
        if (updatedCustomer.getAddress() != null) {
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }
        return customerRepository.save(existingCustomer);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public void deleteRelatedAccounts(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found with ID: " + customerId));
        List<Account> relatedAccounts = accountRepository.findByCustomer(customer);
        accountRepository.deleteAll(relatedAccounts);
    }

    public void deleteCustomer(Long customerId) {
        deleteRelatedAccounts(customerId);
        customerRepository.deleteById(customerId);
    }

}
