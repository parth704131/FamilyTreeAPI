package com.marketpulse.FamilyTree.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true)
    String name;
    @Enumerated(EnumType.ORDINAL)
    Gender gender;
    @JsonIgnore
    @OneToOne(optional = true,fetch = FetchType.LAZY)
    HouseHold family;
    @JsonIgnore
    @OneToOne(optional = true,fetch = FetchType.LAZY)
    HouseHold parents;

    public Person(){

    }
    public Person(String name,Gender gender) {
        this.name=name;
        this.gender=gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public HouseHold getFamily() {
        return family;
    }

    public void setFamily(HouseHold family) {
        this.family = family;
    }

    public HouseHold getParents() {
        return parents;
    }

    public void setParents(HouseHold parents) {
        this.parents = parents;
    }

}
