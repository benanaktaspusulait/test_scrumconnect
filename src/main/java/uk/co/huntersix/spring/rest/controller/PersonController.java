package uk.co.huntersix.spring.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.util.Constants;
import uk.co.huntersix.spring.rest.exception.DuplicateResourceException;
import uk.co.huntersix.spring.rest.exception.ResourceNotFoundException;
import uk.co.huntersix.spring.rest.payload.GeneralResponseDTO;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/persons")
public class PersonController {

    @Autowired
    private PersonDataService personDataService;

    @GetMapping
    public List<Person> findAll() {
        return personDataService.findAll();
    }

    @GetMapping("/{firstName}/{lastName}")
    public Person findAll(@PathVariable(value = "firstName") String firstName,
                          @PathVariable(value = "lastName") String lastName) throws ResourceNotFoundException {

        log.debug("REST request to get getAll firstname : {}, lastname : {}", firstName, lastName);
        return personDataService.find(firstName, lastName);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid Person person) throws DuplicateResourceException {

        log.debug("REST request to save Person: {}", person);
        person = personDataService.save(person);
        return new ResponseEntity<Person>(person, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody @Valid Person person) throws ResourceNotFoundException {

        log.debug("REST request to update Person: {}", person);
        person = personDataService.update(person);
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ResourceNotFoundException {

        log.debug("REST request to delete Person with id : {}", id);
        personDataService.delete(id);
        return new ResponseEntity<>(new GeneralResponseDTO("deleted"), HttpStatus.ACCEPTED);
    }


}