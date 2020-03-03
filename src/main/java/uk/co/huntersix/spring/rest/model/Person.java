package uk.co.huntersix.spring.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private final AtomicLong counter = new AtomicLong();

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public Person(String firstName, String lastName) {
        this.id = counter.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName(){
        return String.format("firstname : %s , lastname : %s", firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
