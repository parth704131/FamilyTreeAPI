package com.marketpulse.FamilyTree.services;

import com.marketpulse.FamilyTree.models.Gender;
import com.marketpulse.FamilyTree.models.HouseHold;
import com.marketpulse.FamilyTree.models.Person;
import com.marketpulse.FamilyTree.models.Relation;
import com.marketpulse.FamilyTree.repositories.HouseHoldRepository;
import com.marketpulse.FamilyTree.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    HouseHoldRepository houseHoldRepository;

    Logger logger = LoggerFactory.getLogger(PersonService.class);

    public boolean createPerson(Person person) {
        if (!person.getName().trim().isEmpty() && person.getName() != null
                && person.getGender() != null) {
            try {
                personRepository.save(person);
                return true;
            } catch (Exception e) {
                logger.error("person object is not saved" + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean addRelationShipBetweenTwoPerson(int person1Id, Relation relation, int person2ID) {
        try {
            Person person1 = personRepository.findById(person1Id).get();
            Person person2 = personRepository.findById(person2ID).get();
            if (person1 != null && person2 != null) {
                if (relation == Relation.SON) {
                    return addChild(person2, person1);
                } else if (relation == Relation.DAUGHTER) {
                    return addChild(person2, person1);
                } else if (relation == Relation.FATHER) {
                    return addParent(person1, person2);
                } else if (relation == Relation.SPOUSE) {
                    return addSpouse(person1, person2);
                } else if (relation == Relation.MOTHER) {
                    return addParent(person1, person2);
                } else {
                    return false;
                }
            }
        }catch (Exception e){
            logger.error("something went wrong at relation time"+e);
            return false;
        }
        return false;
    }

    private boolean addSpouse(Person person1, Person person2) {
        try {
            HouseHold family = new HouseHold(person1, person2);
            houseHoldRepository.save(family);
            person1.setFamily(family);
            person2.setFamily(family);
            personRepository.save(person1);
            personRepository.save(person2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addChild(Person person, Person child) {
        return saveParentChildRelationship(person,child);
    }

    public boolean addParent(Person parent, Person child) {
       return saveParentChildRelationship(parent,child);
    }

    public boolean saveParentChildRelationship(Person parent,Person child){
        try {
            child.setParents(parent.getFamily());
            parent.getFamily().getChildren().add(child);
            personRepository.save(child);
            personRepository.save(parent);
            return true;
        } catch (Exception e) {
            logger.error("error while giving parent child relationship " + e);
            return false;
        }
    }

    public int getNumOfSons(String name) {
        int numberOfSon = 0;
        Person person = personRepository.findByName(name);
        if (person.getFamily() != null && person!=null) {
            List<Person> children = person.getFamily().getChildren();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getGender() == Gender.MALE) {
                    numberOfSon++;
                }
            }
        }
        return numberOfSon;
    }

    public int getNumOfDaughters(String name) {
        int numOfDaughters = 0;
        Person person = personRepository.findByName(name);
        if (person.getFamily() != null && person !=null) {
            List<Person> children = person.getFamily().getChildren();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getGender() == Gender.FEMALE) {
                    numOfDaughters++;
                }
                if (children.get(i).getFamily() != null) {
                    numOfDaughters += getNumOfDaughters(children.get(i).getName());
                }
            }
        }
        return numOfDaughters;
    }

    public Person getPersonById(int id) {
        try {
            return personRepository.findById(id).get();
        }catch (Exception e){
            logger.error("person not found");
            return null;
        }
    }
}
