package ni40034974.track2.bank.ni40034974_bank.controller;


import ni40034974.track2.bank.ni40034974_bank.model.Customer;
import ni40034974.track2.bank.ni40034974_bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody Customer updatedCustomer) {
        Customer updated = customerService.updateCustomer(customerId, updatedCustomer);
        return ResponseEntity.ok(updated);
    }

    @GetMapping()
    public ResponseEntity<?> getCustomer() {
        List<Customer> Customerlist = customerService.getCustomers();
        if(!Customerlist.isEmpty())
            return ResponseEntity.ok(Customerlist);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }
}
