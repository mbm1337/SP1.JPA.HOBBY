package org.hobby.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

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
    @JoinColumn(name = "zip")
    private ZipCode zipCode;

    @ManyToMany(mappedBy = "persons")
    private HashSet<Hobby> hobbies = new HashSet<>();
    public void addAddresstoPerson(ZipCode zipCode){
        this.setZipCode(zipCode);
    }
    public void addHobby(Hobby hobby){
        if(hobby != null)
        hobbies.add(hobby);
        }






    public Person(String firstName, String lastName, int age, Gender gender, String phone, String email,String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address= address;
    }












}
