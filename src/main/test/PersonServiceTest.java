import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PersonServiceTest {

    @Test
    public void testGetPersonsByCity() {
        // Mocking the PersonDAO
        PersonDAO personDAO = mock(PersonDAO.class);

        // Creating a sample list of persons
        List<Person> personsInCity = new ArrayList<>();
        // Populate the list with sample persons

        // Stubbing the behavior of the DAO method
        when(personDAO.getPersonsByCity("2800 Lyngby")).thenReturn(personsInCity);

        // Creating the service instance
        PersonService personService = new PersonService(personDAO);

        // Calling the service method
        List<Person> result = personService.getPersonsByCity("2800 Lyngby");

        // Assertions
        assertEquals(personsInCity, result);
        verify(personDAO).getPersonsByCity("2800 Lyngby"); // Verify the method call
    }
}
