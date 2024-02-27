package org.hobby.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @ManyToOne
    private ZipCode ZipCode;

    @ManyToMany(mappedBy = "persons")
    private List<Hobby> hobbies;

    public Person(String firstName, String lastName, Date birthDate, Gender gender, String phone, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public void addHobby(Hobby hobby) {
        this.hobbies.add(hobby);
        if (hobby != null) {
            hobby.getPersons().add(this);
        }
    }

    public void removeHobby(Hobby hobby) {
        this.hobbies.remove(hobby);
        if (hobby != null) {
            hobby.getPersons().remove(this);
        }
    }

    public void setZipCode(ZipCode zipCode) {
        ZipCode = zipCode;
        if (zipCode != null) {
            zipCode.getPersons().add(this);
        }
    }

    public void removeZipCode(ZipCode zipCode) {
        ZipCode = zipCode;
        if (zipCode != null) {
            zipCode.getPersons().remove(this);
        }
    }

}
