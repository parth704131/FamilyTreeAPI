package com.marketpulse.FamilyTree.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class HouseHold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToOne
    Person husband;
    @OneToOne
    Person wife;
    @OneToMany
    List<Person> children;

    HouseHold(Person person1,Person person2){
        this.husband=person1.getGender().equals(Gender.MALE)?person1:person2;
        this.wife=person1.getGender().equals(Gender.FEMALE)?person1:person2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getHusband() {
        return husband;
    }

    public void setHusband(Person husband) {
        this.husband = husband;
    }

    public Person getWife() {
        return wife;
    }

    public void setWife(Person wife) {
        this.wife = wife;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }
}
