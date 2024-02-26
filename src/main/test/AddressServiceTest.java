import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AddressServiceTest {

    @Test
    public void testGetAllPostcodesAndCitiesInDenmark() {
        // Mocking the AddressDAO
        AddressDAO addressDAO = mock(AddressDAO.class);

        // Creating a sample list of postcodes and city names
        List<Object[]> postcodesAndCitiesInDenmark = new ArrayList<>();
        // Populate the list with sample postcodes and city names

        // Stubbing the behavior of the DAO method
        when(addressDAO.getAllPostcodesAndCitiesInDenmark()).thenReturn(postcodesAndCitiesInDenmark);

        // Creating the service instance
        AddressService addressService = new AddressService(addressDAO);

        // Calling the service method
        List<Object[]> result = addressService.getAllPostcodesAndCitiesInDenmark();

        // Assertions
        assertEquals(postcodesAndCitiesInDenmark, result);
        verify(addressDAO).getAllPostcodesAndCitiesInDenmark(); // Verify the method call
    }
}
