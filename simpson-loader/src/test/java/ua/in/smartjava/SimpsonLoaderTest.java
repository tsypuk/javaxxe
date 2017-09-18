package ua.in.smartjava;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.util.List;

public class SimpsonLoaderTest {

    private SimpsonLoader simpsonLoader;

    @Before
    public void init() {
        simpsonLoader = new SimpsonXmlLoader(false);
    }

    @Test
    public void testLoadSimpsons() {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("simpsons.xml").getFile());
        Simpson homer = Simpson.of().firstName("Homer")
                .lastName("Simpson").nickName("nuclear").salary("1").id("1001")
                .password("pass").about("About this person")
                .build();
        Simpson bart = Simpson.of().firstName("Bart")
                .lastName("Simpson").nickName("skate").salary("2").id("2001")
                .password("pass").about("About this person")
                .build();
        Simpson lisa = Simpson.of().firstName("Lisa")
                .lastName("Simpson").nickName("dummy").salary("2").id("3001")
                .password("pass").about("About this person")
                .build();
        Simpson maggie = Simpson.of().firstName("Maggie")
                .lastName("Simpson").nickName("hairdo").salary("2").id("4001")
                .password("pass").about("About this person")
                .build();

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals(4, simpsons.size());
        assertThat(simpsons, contains(homer, bart, lisa, maggie));
    }

}
