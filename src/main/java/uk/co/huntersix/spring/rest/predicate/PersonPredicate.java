package uk.co.huntersix.spring.rest.predicate;

import uk.co.huntersix.spring.rest.model.Person;

import java.util.function.Predicate;

public class PersonPredicate {

    public static Predicate<Person> filterByFirstNameAndLastName(Person person){
        return pr -> (pr.getFirstName().equals(person.getFirstName()) &&  pr.getLastName().equals(person.getLastName()));
    }

    public static Predicate<Person> filterById(Long id){
        return pr -> (pr.getId().equals(id));
    }

}
