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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public boolean addRelationShipBetweenTwoPerson(int person1Id, Relation relation, int person2Id,int person3Id) {
        try {
            Person person1 = personRepository.findById(person1Id).get();
            Person person2 = personRepository.findById(person2Id).get();
            if(person3Id==0 && relation==Relation.SPOUSE){
                return addSpouse(person1, person2);
            }else if (person1 != null
                        && person2 != null
                        && person3Id!=0
                        && (relation==Relation.SON || relation==Relation.DAUGHTER) ) {
                Person person3=personRepository.findById(person3Id).get();
                return addChildRelationShip(person2Id,person3Id,person1Id);
            }
        }catch (Exception e){
            logger.error("something went wrong at relation time"+e);
            return false;
        }
        return false;
    }

    public boolean addChildRelationShip(int fatherId,int motherId,int childId){
        Person father=personRepository.findById(fatherId).get();
        Person child=personRepository.findById(childId).get();

        HouseHold family=null;

        List<HouseHold> families=father.getFamilies();
        for (int i=0;i<families.size();i++)
        {
            if(families.get(i).getWife().getId()==motherId){
                family=families.get(i);
                break;
            }
        }
        family.getChildren().add(child);
        houseHoldRepository.save(family);
        child.setParents(family);
        personRepository.save(child);
        return true;
    }

    private boolean addSpouse(Person person1, Person person2) {
        try {
            HouseHold family = new HouseHold(person1, person2);
            person1.getFamilies().add(family);
            person2.getFamilies().add(family);
            houseHoldRepository.save(family);
            personRepository.save(person1);
            personRepository.save(person2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getNumOfSons(String name) {
        int numberOfSon = 0;
        Person person = personRepository.findByName(name);
        if (person.getFamilies().size()>0 && person!=null) {
            List<HouseHold> families = person.getFamilies();
            for (int i = 0; i < families.size(); i++) {
                for(int j=0;j<families.get(i).getChildren().size();j++){
                    if(families.get(i).getChildren().get(j).getGender()==Gender.MALE){
                        numberOfSon++;
                    }
                }
            }
        }
        return numberOfSon;
    }

    public int getNumOfDaughters(String name) {
        Person person=personRepository.findByName(name);
        return getNumOfDaughters(person);
    }

    public int getNumOfDaughters(Person person){
        int numOfDaughters = 0;
        if (person.getFamilies().size() >0 && person !=null) {
            List<HouseHold> families = person.getFamilies();
            for (int i = 0; i < families.size(); i++) {
                for(int j=0;j<families.get(i).getChildren().size();j++) {
                    if (families.get(i).getChildren().get(j).getGender() == Gender.FEMALE) {
                        numOfDaughters++;
                    }
                    if (families.get(i).getChildren().get(j).getFamilies().size()>0) {
                        numOfDaughters += getNumOfDaughters(families.get(i).getChildren().get(j));
                    }
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

    public int getNumOfWives(int id){
        int numOfWives=0;
        try{
            Person person=personRepository.findById(id).get();
            if(person.getGender()== Gender.MALE){
                return person.getFamilies().size();
            }
            return numOfWives;
        }catch (Exception e){
            return numOfWives;
        }
    }

    public int getNumOfUncles(int id) {
        int numOfUncles=0;
        try{
            Person person=personRepository.findById(id).get();
            HouseHold parents=person.getParents();
            HouseHold grandParents=parents.getHusband().getParents();
            for (int i=0;i<grandParents.getChildren().size();i++){
                if(grandParents.getChildren().get(i).getGender()==Gender.MALE){
                    numOfUncles++;
                }
            }
            return numOfUncles-1;
        }catch (Exception e){
            return numOfUncles;
        }
    }

    public boolean checkPersonHaveRelation(int person1ID, int person2ID) {
        Person person1=personRepository.findById(person1ID).get();
        Person person2=personRepository.findById(person2ID).get();
        if(person1.getParents()!=null && person2.getParents()!=null){
            if(person1.getParents().getId()==person2.getParents().getId()){
                return true;
            }
            return false;
        }
        return false;
    }

    public int getNumOfImmediateCousins(int id) {
        int numOfCousins=0;
        Person person=personRepository.findById(id).get();
        HouseHold parents=person.getParents();
        if(parents!=null){
            if(parents.getHusband().getParents()!=null){
                Person husband=parents.getHusband();
                numOfCousins = getNumOfCousins(husband);
                return numOfCousins;
            }else if(parents.getWife().getParents()!=null){
                Person wife=parents.getWife();
                numOfCousins=getNumOfCousins(wife);
                return numOfCousins;
            }
        }
        return numOfCousins;
    }

    public int getNumOfCousins(Person person){
        int numOfCousins=0;
        HouseHold gradParents=person.getParents();
        for(int i=0;i<gradParents.getChildren().size();i++){
            if(person.getId()!=gradParents.getChildren().get(i).getId()){
                List<HouseHold> families=gradParents.getChildren().get(i).getFamilies();
                if(families.size()>0){
                    for(int j=0;j<families.size();j++){
                        numOfCousins+=families.get(j).getChildren().size();
                    }
                    return numOfCousins;
                }
            }
        }
        return  numOfCousins;
    }
}
