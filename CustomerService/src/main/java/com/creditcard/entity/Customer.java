package com.creditcard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @NotNull(message = "Customer ID is required")
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must be less than 100 characters")
    @Column(name = "CUSTOMER_NAME", nullable = false, length = 100)
    private String customerName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Email address is invalid")
    @Size(max = 100, message = "Email address must be less than 100 characters")
    @Column(name = "EMAIL_ADDRESS", nullable = false, unique = true, length = 100)
    private String emailAddress;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    @Column(name = "MOBILE_NUMBER", nullable = false, length = 15)
    private String mobileNumber;

    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN number must be valid")
    @Column(name = "PAN_NUMBER", nullable = false, unique = true, length = 10)
    private String panNumber;

    public Customer() {
    }

    public Customer(Long customerId, String customerName, String emailAddress, String mobileNumber, String panNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.panNumber = panNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }
}