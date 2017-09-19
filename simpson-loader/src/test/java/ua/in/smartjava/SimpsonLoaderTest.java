package ua.in.smartjava;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class SimpsonLoaderTest {

    private static final String PASSWORD = "PASSWORD";
    private SimpsonLoader simpsonLoader;

    @Before
    public void init() {
        simpsonLoader = SimpsonXmlLoader.builder().build();
    }

    @Test
    public void testLoadSimpsons() {
        // Given
        File file = getFile("1-simpsons.xml");

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

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    @Test
    public void testScanFileSystem() {
        // Given
        File file = getFile("2-simpsons-fs-scan.xml");
        File[] files = new File("/etc").listFiles();

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        String injectedFS = simpsons.get(1).getPassword();
        String[] split = injectedFS.split("\n");

        assertTrue(files.length > 0);
        assertEquals(files.length, split.length);
    }

    @Test
    public void testFileContentRead() throws FileNotFoundException {
        // Given
        File file = getFile("4-simpsons-fs-password.xml");
        writePasswordToFile("/tmp/password.txt");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals(PASSWORD, simpsons.get(3).getPassword());
    }

    private void writePasswordToFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        PrintStream out = new PrintStream(new FileOutputStream(fileName));
        out.print(PASSWORD);
    }

    @Test
    public void testMillionLaughs() {
        // Given
        File file = getFile("5-simpsons-xml-bomb.xml");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
    }

}
