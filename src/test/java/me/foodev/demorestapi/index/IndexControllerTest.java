package me.foodev.demorestapi.index;

import me.foodev.demorestapi.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest // 해당 어노테이션은 DemoRestApiApplication에서 빈을 다 읽어서 실행
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)//스프링 설정파일을 읽어와서 사용하는 방법
@ActiveProfiles("test") //h2를 사용하기 위한 설정 해당 에노테이션을 사용하면 테스트 applicaition-test.properties 를 사용
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    public void index() throws  Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }

}

