package com.marketpulse.FamilyTree.services;

import com.marketpulse.FamilyTree.models.Gender;
import com.marketpulse.FamilyTree.models.HouseHold;
import com.marketpulse.FamilyTree.models.Person;
import com.marketpulse.FamilyTree.models.Relation;
import com.marketpulse.FamilyTree.repositories.HouseHoldRepository;
import com.marketpulse.FamilyTree.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    HouseHoldRepository houseHoldRepository;

    public boolean createPerson(Person person){
        if(!person.getName().trim().isEmpty() && person.getName()!=null
            && person.getGender()!=null) {
            try {
                personRepository.save(person);
                return  true;
            } catch (Exception e){
                System.out.println(e);
                return false;
            }
        }
        return false;
    }

    public void addRelationShipBetweenTwoPerson(int person1Id, Relation relation, int person2ID) {
        Person person1=personRepository.findById(person1Id).get();
        Person person2=personRepository.findById(person2ID).get();
        System.out.println(relation);
        if (relation==Relation.SON){
            addChild(person2,person1);
        }else if (relation==Relation.DAUGHTER){
            addChild(person2, person1);
        }else if(relation==Relation.FATHER){
            person2.getFamily().setHusband(person1);
            person1.getFamily().getChildren().add(person2);
        }else  if(relation==Relation.SPOUSE){
            addSpouse(person1,person2);
        }else if(relation==Relation.MOTHER){
            person2.getFamily().setWife(person1);
            person1.getFamily().getChildren().add(person2);
        }
    }

    private void addSpouse(Person person1, Person person2) {
        HouseHold family=new HouseHold(person1,person2);
        houseHoldRepository.save(family);
        person1.setFamily(family);
        person2.setFamily(family);
        personRepository.save(person1);
        personRepository.save(person2);
    }

    public void addChild(Person person,Person child){
        person.getFamily().getChildren().add(child);
        child.setParents(person.getFamily());
        personRepository.save(person);
        personRepository.save(child);
    }

    public int getNumOfSons(int personId){
        int numberOfSon=0;
        Person person=personRepository.findById(personId).get();
        List<Person> children=person.getFamily().getChildren();
        for(int i=0;i<children.size();i++){
            if(children.get(i).getGender()==Gender.MALE){
                numberOfSon++;
            }
        }
        return numberOfSon;
    }
}
