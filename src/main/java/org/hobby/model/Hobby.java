package org.hobby.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hobby")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "wikilink")
    private String wikiLink;

    @Column(name = "category")
    private String category;

    @Column(name = "type")
    private String type;

    @ManyToMany
    private List<Person> persons;

    public Hobby(String name, String wikiLink, String category, String type) {
        this.name = name;
        this.wikiLink = wikiLink;
        this.category = category;
        this.type = type;
    }


}
