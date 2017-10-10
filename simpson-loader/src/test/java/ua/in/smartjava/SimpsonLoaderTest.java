package ua.in.smartjava;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
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
    private SimpsonLoader securedSimpsonLoader;
    private SimpsonLoader accumulatedSimpsonLoader;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9999);

    @Before
    public void init() {
        simpsonLoader = SimpsonXmlLoader.builder().build();
        securedSimpsonLoader = SimpsonXmlLoader.builder().secured(true).build();
        accumulatedSimpsonLoader = SimpsonXmlLoader.builder().increaseAccumulatedSize(true).build();
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

    /**
     * [Fatal Error] :1:1: JAXP00010001: The parser has encountered more than "64000" entity expansions in this document;
     * this is the limit imposed by the JDK.
     * Exception: JAXP00010001: The parser has encountered more than "64000" entity expansions in this document;
     * this is the limit imposed by the JDK.
     */
    @Test
    public void testMillionLaughs() {
        // Given
        File file = getFile("5-simpsons-xml-bomb.xml");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals(0, simpsons.size());
    }

    @Ignore
    @Test
    public void testMillionLaughsAccumulated() {
        // Given
        File file = getFile("5-simpsons-xml-bomb.xml");

        // When
        List<Simpson> simpsons = accumulatedSimpsonLoader.loadFromFile(file);

        // Then
        assertEquals(0, simpsons.size());
    }

    @Test
    public void testDtdInvocation() {
        // Given
        stubFor(get("/evil2.dtd")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        File file = getFile("simpsons-dtd.xml");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        verify(getRequestedFor(urlMatching("/evil2.dtd"))
                .withHeader("user-agent", matching("Java/.*")));
    }

    @Test
    public void testJavaChampions() {
        //Given
        File file = getFile("simpsons-java-champions.xml");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals("list of java-champions: \\n\n" +
                "Josh Bloch \\n\n" +
                "Yakov Fain \\n", simpsons.get(1).getPassword());
    }

    @Test
    public void testXmlBombExplained() {
        //Given
        File file = getFile("simpsons-xml-bomb-explain.xml");

        // When
        List<Simpson> simpsons = simpsonLoader.loadFromFile(file);

        // Then
        assertEquals(3*3*3, getWordsCount(simpsons.get(0).getPassword()));
    }

    private int getWordsCount(String input) {
        return input.split(", ").length;
    }

    @Test
    public void testXmlBombExplainedSecured() {
        //Given
        File file = getFile("simpsons-xml-bomb-explain.xml");

        // When
        List<Simpson> simpsons = securedSimpsonLoader.loadFromFile(file);

        // Then
        assertEquals(0, simpsons.size());
    }

    @Test
    public void testSecuredJavaChampions() {
        //Given
        File file = getFile("simpsons-java-champions.xml");

        // When
        List<Simpson> simpsons = securedSimpsonLoader.loadFromFile(file);

        // Then
        assertEquals(0, simpsons.size());
    }

    @Test
    public void testSecuredDtdInvocation() {
        // Given
        stubFor(get("/evil2.dtd")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        File file = getFile("simpsons-dtd.xml");

        // When
        List<Simpson> simpsons = securedSimpsonLoader.loadFromFile(file);

        // Then
        verify(0, getRequestedFor(urlMatching("/evil2.dtd"))
                .withHeader("user-agent", matching("Java/.*")));
    }

}
