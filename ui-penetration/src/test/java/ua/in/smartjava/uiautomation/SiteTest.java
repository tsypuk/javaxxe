package ua.in.smartjava.uiautomation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.openqa.selenium.support.FindBy;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Slf4j
@RunWith(JUnit4.class)
public class SiteTest {

    @Rule
    public GenericContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
//            add files to resources
//            .withClasspathResourceMapping()
            .withRecordingMode(RECORD_ALL, new File("./"));

    @Rule
    public GenericContainer simpsonServiceJre8 = new GenericContainer("simpson-service.jre8:latest").withExposedPorts(8080);

    protected String getAppUrl() {
        final String appUrl = "http://" + simpsonServiceJre8.getContainerIpAddress() + ":" + simpsonServiceJre8.getMappedPort(8080);
        log.info("Returning default app url as: {}", appUrl);
        return appUrl;
    }

//    DTDFakeServer. ip

//    resource/aaa.xml

    @FindBy(linkText = "Upload")
    private WebElement uploadLink;

    @FindBy(linkText = "Simpsons list")
    private WebElement listLink;

    @FindBy(linkText = "Simpsons table")
    private WebElement tableLink;

    //TODO Parametrize every method to call different containers.
    @Test
//    @Ignore
    public void testCallWikipedia() throws InterruptedException {
        RemoteWebDriver driver = ((BrowserWebDriverContainer)chrome).getWebDriver();
        driver.get("https://wikipedia.org");
        WebElement searchInput = driver.findElementByName("search");

        searchInput.sendKeys("JavaOne2017");
        searchInput.submit();

//        WebElement otherPage = driver.findElementByLinkText("Rickrolling");
//        otherPage.click();

//        boolean expectedTextFound = driver.findElementsByCssSelector("p")
//                .stream()
//                .anyMatch(element -> element.getText().contains("meme"));
//
//        assertTrue("The word 'meme' is found on a page about rickrolling", expectedTextFound);
    }

//TODO add ui tests for every XXE scenarious

// Filesystem scan

    @Test
    public void testScanFS() throws InterruptedException {
        Thread.sleep(20000);
        RemoteWebDriver driver = ((BrowserWebDriverContainer)chrome).getWebDriver();
        driver.get(getAppUrl());
//        uploadLink.sendKeys("/tmp/1.txt");
//        uploadLink.click();
        listLink.click();
        Thread.sleep(3000);
        tableLink.click();
        Thread.sleep(3000);

//        WebElement otherPage = driver.findElementByLinkText("Rickrolling");
//        otherPage.click();

//        boolean expectedTextFound = driver.findElementsByCssSelector("p")
//                .stream()
//                .anyMatch(element -> element.getText().contains("meme"));
//
//        assertTrue("The word 'meme' is found on a page about rickrolling", expectedTextFound);
    }

// read file

// call http service

// xmlbomb

// send file to server

}