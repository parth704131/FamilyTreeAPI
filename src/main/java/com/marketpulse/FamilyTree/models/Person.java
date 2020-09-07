package com.marketpulse.FamilyTree.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(targetEntity=HouseHold.class,cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    List<HouseHold> houseHold=new ArrayList<>();
    @JsonIgnore
    @OneToOne(optional = true,fetch = FetchType.EAGER)
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

    public List<HouseHold> getFamilies() {
        return houseHold;
    }

    public void setFamilies(List<HouseHold> families) {
        this.houseHold = families;
    }

    public HouseHold getParents() {
        return parents;
    }

    public void setParents(HouseHold parents) {
        this.parents = parents;
    }

}
