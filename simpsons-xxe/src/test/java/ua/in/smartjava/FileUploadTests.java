package ua.in.smartjava;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class FileUploadTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SimpsonService simpsonService;

    @Test
    public void uploadedFile() throws Exception {
        final String xmlContent = "<?xml version=\"1.0\"?>\n" +
                "<company>\n" +
                "<staff id=\"1001\">\n" +
                "<firstname>Homer</firstname>\n" +
                "<lastname>Simpson</lastname>\n" +
                "<nickname>nuclear</nickname>\n" +
                "<salary>1</salary>\n" +
                "<password>pass</password>\n" +
                "</staff>\n" +
                "</company>";
        MockMultipartFile multipartFile =
                new MockMultipartFile("file",
                        "test.txt",
                                "text/xml, application/xml",
                        xmlContent.getBytes());
        ResultActions actions = mvc.perform(fileUpload("/").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/table"));

        actions.andDo(MockMvcResultHandlers.print());

        then(this.simpsonService).should().loadFromFile(multipartFile);
    }
}
