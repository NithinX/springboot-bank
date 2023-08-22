package ni40034974.track2.bank.ni40034974_bank.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ni40034974.track2.bank.ni40034974_bank.address.Address;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
    @Transient
    private Integer age;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Contact number is required")
    @Column(unique = true)
    private String contact;
    @NotNull(message = "Address is required")
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    public Customer(String firstName, String lastName, LocalDate dob, String email, String contact, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    @SuppressWarnings("unused")
    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }


}
