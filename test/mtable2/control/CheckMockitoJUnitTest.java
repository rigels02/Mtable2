package mtable2.control;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author raitis
 */
public class CheckMockitoJUnitTest {

    public CheckMockitoJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void helloMock() {
        // mock creation
        List mockedList = mock(List.class);

// using mock object - it does not throw any "unexpected interaction" exception
        mockedList.add("one");
        mockedList.clear();

// selective, explicit, highly readable verification
        verify(mockedList).add("one");
        verify(mockedList).clear();
        //--------------------------
        // stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");

// the following prints "first"
        System.out.println(mockedList.get(0));

// the following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));
    }
}
