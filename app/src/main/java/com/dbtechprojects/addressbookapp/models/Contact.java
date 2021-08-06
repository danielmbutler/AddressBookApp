package com.dbtechprojects.addressbookapp.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey
    @NonNull
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String address;
    public String city;
    public String postcode;
    public String dob;

    public Contact(
            @NonNull String id,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String city,
            String postcode,
            String dob

    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }
}
