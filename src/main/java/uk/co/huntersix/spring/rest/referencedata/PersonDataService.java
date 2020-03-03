package uk.co.huntersix.spring.rest.referencedata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.DuplicateResourceException;
import uk.co.huntersix.spring.rest.exception.ResourceNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.predicate.PersonPredicate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonDataService {

    public static final List<Person> PERSON_DATA = Arrays.asList(
            new Person(1L, "Mary", "Smith"),
            new Person(2L,"Brian", "Archer"),
            new Person(3L,"Collin", "Brown")
    );

    public Person find(String firstName, String lastName ) throws ResourceNotFoundException {

        Person person = PERSON_DATA.stream()
                .filter(PersonPredicate.filterByFirstNameAndLastName(new Person(firstName, lastName)))
                .findAny()
                .orElse(null);

        return Optional
                .ofNullable(person)
                .orElseThrow(() -> new ResourceNotFoundException
                        (person.getFirstName().concat(", ").concat(person.getLastName()).concat(" was not found")));

    }

    public List<Person> findAll(String firstName, String lastName) {

        log.debug("get getAll firstname : {}, lastname : {}",  firstName, lastName);

        return PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());
    }

    public List<Person> findAll() {

        return PERSON_DATA;
    }


    public Person save(Person person) throws DuplicateResourceException {

        boolean personExist = PERSON_DATA.stream().anyMatch(PersonPredicate.filterByFirstNameAndLastName(person));

        if (personExist) {
            throw new DuplicateResourceException(person.getFullName().concat(" already exist"));
        }
        person = new Person(person.getFirstName(), person.getLastName());
        PERSON_DATA.stream().collect(Collectors.toList()).add(person);
        return person;
    }

    public Person findById(Long id) throws ResourceNotFoundException {

        Person person = PERSON_DATA.stream()
                .filter(PersonPredicate.filterById(id))
                .findAny()
                .orElse(null);

        return Optional
                .ofNullable(person)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Person with id: ".concat(String.valueOf(id)).concat(" was not found")));
    }

    public Person update(Person person) throws ResourceNotFoundException {

        Person personInDb = findById(person.getId());

        PERSON_DATA.remove(personInDb);
        personInDb.setFirstName(person.getFirstName());
        personInDb.setLastName(person.getLastName());
        PERSON_DATA.add(personInDb);

        return personInDb;
    }

    public void delete(Long id) throws ResourceNotFoundException {

        Person personInDb = findById(id);
        PERSON_DATA.remove(personInDb);
    }

}
