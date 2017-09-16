package ua.in.smartjava;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimpsonService {

    private List<Simpson> simpsons = new ArrayList<Simpson>();

    List<Simpson> getSimpsons() {
        return simpsons;
    }

    void loadFromFile(MultipartFile multipartFile) {
        simpsons.clear();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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
                    simpson.setPassword(eElement.getElementsByTagName("password").item(0).getTextContent());
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