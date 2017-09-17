package ua.in.smartjava;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LibraryTest {

    private SimpsonLoader simpsonLoader;

    @Before
    public void init() {
        simpsonLoader = new SimpsonXmlLoader(false);
    }

    @Test
    public void testLoadStaff() {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("staff.xml").getFile());

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals(4, simpsons.size());
        assertEquals("1001", simpsons.get(0).getId());
    }

}
