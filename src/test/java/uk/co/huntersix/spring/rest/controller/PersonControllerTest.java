package uk.co.huntersix.spring.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.co.huntersix.spring.constants.ConstantsTest;
import uk.co.huntersix.spring.rest.util.Constants;
import uk.co.huntersix.spring.rest.exception.DuplicateResourceException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnAllPersons() throws Exception {
        this.mockMvc.perform(get(Constants.API_PREFIX + ConstantsTest.URL_PERSONS))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotReturnPersonFromService() throws Exception {

        when(personDataService.find(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get(Constants.API_PREFIX + ConstantsTest.URL_PERSONS + "/Mary/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnEmptyPersonListFromService() throws Exception {
        when(personDataService.findAll(any(), any())).thenReturn(Lists.emptyList());
        this.mockMvc.perform(get(Constants.API_PREFIX + ConstantsTest.URL_PERSONS  + "/jack/mary"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnPersonListFromService() throws Exception {

        Person person = new Person("Abraham", "Lincoln");
        String inputJson = mapToJson(person);

        this.mockMvc.perform( MockMvcRequestBuilders
                .post(Constants.API_PREFIX + ConstantsTest.URL_PERSONS)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnDuplicatePersonFromService() throws Exception {

        Person person = new Person("Smith", "Mary");
        doThrow(new DuplicateResourceException("firstname : Smith , lastname : Mary already exist")).when(personDataService).save(person);
        String inputJson = mapToJson(person);

        this.mockMvc.perform( MockMvcRequestBuilders
                .post(Constants.API_PREFIX + ConstantsTest.URL_PERSONS)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value(person.getFullName().concat(" already exist")));
    }

    @Test
    public void deletePerson() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.delete(Constants.API_PREFIX + ConstantsTest.URL_PERSONS + "/{id}", 1) )
                .andExpect(status().isAccepted());
    }

}