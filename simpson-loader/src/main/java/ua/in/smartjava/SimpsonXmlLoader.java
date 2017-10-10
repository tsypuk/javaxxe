package ua.in.smartjava;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * This class is using syntax of Java5 lang compatibility.
 * Will be used to verify vulnerabilities of xml parser on core java5-java8.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpsonXmlLoader implements SimpsonLoader {

    private boolean secured;
    private boolean increaseAccumulatedSize;

    public List<Simpson> loadFromFile(File file) {
        List<Simpson> simpsons = new ArrayList<Simpson>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            setupDocumentBuilderFactory(documentBuilderFactory);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("staff");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Simpson simpson = new Simpson();
                    simpson.setId(eElement.getAttribute("id"));
                    simpson.setFirstName(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    simpson.setLastName(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    simpson.setNickName(eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    simpson.setSalary(eElement.getElementsByTagName("salary").item(0).getTextContent());
                    simpson.setAbout(eElement.getElementsByTagName("about").item(0).getTextContent());
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
//                    int wordsCount = password.split(" ").length;
//                    simpson.setPassword(password + "<BR>" + wordsCount);
                    simpson.setPassword(password);
                    simpsons.add(simpson);
                }
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return simpsons;
    }

    private void setupDocumentBuilderFactory(DocumentBuilderFactory dbFactory) throws ParserConfigurationException {
        if (increaseAccumulatedSize) {
            increaseAccumulatedSize(dbFactory);
        }

        if (secured) {
            secureDocumentBuilder(dbFactory);
        }
    }

    /**
     * Applies features that allow or deny the DTD interpretation, external entities processing and others...
     *
     * @param dbFactory the instance of DocumentBuilderFactory thats features will be set up.
     */
    private void secureDocumentBuilder(DocumentBuilderFactory dbFactory) throws ParserConfigurationException {
        //  We do not allow external entities processing by setting up these flags:
        dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        //  XML Entity Expansion Injection (XML Bomb)
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    }

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
    private void increaseAccumulatedSize(DocumentBuilderFactory dbFactory) throws ParserConfigurationException {
        dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
    }
}
