package com.marketpulse.FamilyTree.controllers;

import com.marketpulse.FamilyTree.models.Person;
import com.marketpulse.FamilyTree.models.Relation;
import com.marketpulse.FamilyTree.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/persons/")
public class FamilyTreeController {

    @Autowired
    PersonService personService;

    @PostMapping("add")
    public ResponseEntity addPerson(@RequestBody Person person) {
        boolean value = personService.createPerson(person);
        if (value) {
            return new ResponseEntity("New Person Created", HttpStatus.OK);
        } else {
            return new ResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{person1-id}/{relation}/{person2-id}")
    public ResponseEntity addRelationBetweenTwoPerson(@PathVariable("person1-id") int person1Id
            , @PathVariable("relation") Relation relation, @PathVariable("person2-id") int person2ID) {
        boolean success=personService.addRelationShipBetweenTwoPerson(person1Id, relation, person2ID);
        if(success){
            return new ResponseEntity("Relation Created", HttpStatus.OK);
        }else {
            return new ResponseEntity("Something went Wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("{name}/sons")
    public ResponseEntity findNumberOfSons(@PathVariable("name") String name) {
        int numberOfSon = personService.getNumOfSons(name);
        return new ResponseEntity(numberOfSon, HttpStatus.OK);
    }

    @GetMapping("{name}/all-daughters")
    public ResponseEntity findNumberOfAllDaughters(@PathVariable("name") String name) {
        int numberOfDaughters = personService.getNumOfDaughters(name);
        return new ResponseEntity(numberOfDaughters, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity getPerson(@PathVariable("id") int id) {
        Person person=personService.getPersonById(id);
        if(person!=null){
            return new ResponseEntity(person,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
