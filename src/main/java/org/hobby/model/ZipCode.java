package org.hobby.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "zipcode")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ZipCode {

    @Id
    @Column(name = "zip")
    private int zip;
    @Column(name = "city_name")
    private String city;
    @Column(name = "region_name")
    private String regionName;
    @Column(name = "municipality_name")
    private String municipalityName;

    @OneToMany(mappedBy = "ZipCode")
    private List<Person> persons;

    public ZipCode(int zip, String city, String regionName, String municipalityName) {
        this.zip = zip;
        this.city = city;
        this.regionName = regionName;
        this.municipalityName = municipalityName;
    }

}
