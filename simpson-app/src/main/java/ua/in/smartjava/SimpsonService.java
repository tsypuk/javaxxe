package ua.in.smartjava;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimpsonService {

    @Value("${secured-:false}")
    private boolean secured;

    private List<Simpson> simpsons = new ArrayList<>();
    private SimpsonLoader simpsonLoader = new SimpsonXmlLoader(secured);

    void loadFromFile(MultipartFile multipartFile) throws IOException {
        this.simpsons = simpsonLoader.loadFromFile(multipartToFile(multipartFile));
    }

    private File multipartToFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
        File convFile = new File("/tmp/uploads/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
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

    public List<Simpson> getSimpsons() {
        return simpsons;
    }
}