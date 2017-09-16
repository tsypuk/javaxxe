package ua.in.smartjava;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimpsonService {

    @Value("${secured}")
    private boolean secured;

    private List<Simpson> simpsons = new ArrayList<>();

    List<Simpson> getSimpsons() {
        return simpsons;
    }

    void loadFromFile(MultipartFile multipartFile) {
        simpsons.clear();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            increaseAccumulatedSize(dbFactory);

            if (secured) {
                secureDocumentBuilder(dbFactory);
            }

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(multipartToFile(multipartFile));
            doc.getDocumentElement().normalize();

            log.info("Root element {}", doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("staff");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                log.info("Current element {}", nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Simpson simpson = new Simpson();
                    simpson.setId(eElement.getAttribute("id"));
                    simpson.setFirstName(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    simpson.setLastName(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    simpson.setNickName(eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    simpson.setSalary(eElement.getElementsByTagName("salary").item(0).getTextContent());
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
                    int wordsCount = password.split(" ").length;
                    simpson.setPassword(password + "<BR>" + wordsCount);
                    simpsons.add(simpson);
                    log.info("Simpson: {}", simpson);
                }
            }
        } catch (Exception e) {
            log.error("Error processing xml file {}", e);
        }
    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File("/tmp/uploads/" + multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    /**
     * Applies features that allow or deny the DTD interpretation, external entities processing and others...
     *
     * @param dbFactory the instance of DocumentBuilderFactory thats features will be set up.
     */
    private void secureDocumentBuilder(DocumentBuilderFactory dbFactory) throws ParserConfigurationException {

/**
 * We do not allow external entities processing by setting up these flags:
 */
        dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

/**
 * XML Entity Expansion Injection (XML Bomb)
 */
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    }

    private void increaseAccumulatedSize(DocumentBuilderFactory dbFactory) throws ParserConfigurationException {
        /**
         * [Fatal Error] :1:1: JAXP00010001:
         * The parser has encountered more than "64000" entity expansions in this document;
         * this is the limit imposed by the JDK (jdk8). To emulate jdk7 or earlier we use:
         * -Xms64m -Xmx2048m -Djdk.xml.entityExpansionLimit=0
         *
         * We need TURN ON this feature to emulate previous version java where
         * [Fatal Error] :1:8: JAXP00010004:
         * The accumulated size of entities is "50,000,006" that exceeded the "50,000,000" limit set by
         * "FEATURE_SECURE_PROCESSING".
         * https://blogs.oracle.com/joew/entry/jdk_7u45_aws_issue_123
         */
        dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
    }

    @PostConstruct
    public void init() {
        Simpson homer = new Simpson();
        homer.setId("0");
        homer.setFirstName("Homer");
        homer.setLastName("J. Simpson");
        homer.setNickName("hsimpson");
        homer.setPassword("Nuclear");
        homer.setSalary("10");
        homer.setAbout("the father, who is overweight, lazy and works at a nuclear power plant and likes doughnuts:)");

        Simpson homer2 = new Simpson();
        homer2.setId("0");
        homer2.setFirstName("Homer");
        homer2.setLastName("J. Simpson");
        homer2.setNickName("hsimpson");
        homer2.setPassword("Nuclear");
        homer2.setSalary("10");
        homer2.setAbout("the father, who is overweight, lazy and works at a nuclear power plant and likes doughnuts:)");

        Simpson homer3 = new Simpson();
        homer3.setId("0");
        homer3.setFirstName("Homer");
        homer3.setLastName("J. Simpson");
        homer3.setNickName("hsimpson");
        homer3.setPassword("Nuclear");
        homer3.setSalary("10");
        homer3.setAbout("the father, who is overweight, lazy and works at a nuclear power plant and likes doughnuts:)");
        simpsons.clear();
        simpsons.add(homer);
        simpsons.add(homer2);
        simpsons.add(homer3);
    }

}