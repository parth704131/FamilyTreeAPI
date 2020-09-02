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
            return new ResponseEntity("something wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{person1-id}/{relation}/{person2-id}")
    public ResponseEntity addRelationBetweenTwoPerson(@PathVariable("person1-id") int person1Id
    , @PathVariable("relation")Relation relation,@PathVariable("person2-id")int person2ID){
        personService.addRelationShipBetweenTwoPerson(person1Id,relation,person2ID);
        return new ResponseEntity("Relation Created",HttpStatus.OK);
    }

    @GetMapping("{id}/sons")
    public ResponseEntity findNumberOfSons(@PathVariable("id") int id){
        int numberOfSon=personService.getNumOfSons(id);
        return  new ResponseEntity(numberOfSon,HttpStatus.OK);
    }

    @GetMapping("{id}/all-daughters")
    public ResponseEntity findNumberOfAllDaughters(@PathVariable("id") int id){
        int numberOfDaughters=personService.getNumOfDaughters(id);
        return new ResponseEntity(numberOfDaughters,HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable("id") int id){
        return personService.getPersonById(id);
    }
}
