package me.foodev.demorestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest // 해당 어노테이션은 DemoRestApiApplication에서 빈을 다 읽어서 실행
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 요청을 만들 수 있고 응답을 검증할 수 있다.
    //스픨ㅇ mvc test에서 가장 핵심인 테스트 어노테이션
    //웹서버를 뛰우지 않기 때문에 조금 빠르지만 디스패처 서블릿을 만들어야 하기 때문에 단위 테스트 보단 느림
    //만드는 객체도 많고 초반 구동이 약간 딜레이가 있다.
    // 단위테스트 보다는 좀 더 걸리고 웹서버를 띄우는것 보단 빠른?리

    @Autowired
    ObjectMapper objectMapper;
    //스프링 부트에 이미 been으로 등록이 되어 있기 대문에 쉽게 사용이 가능하다. 잭슨이 이미 등록 되어 있다.


    @Test
    public void createEvent() throws Exception {
       Event event = Event.builder() //제대로 된 요청을 만들기 위해 사용
               .id(100)// id  값은 자동생성이 되야하는데 값을 입력 받으면 안된다.
               .name("spring")
               .description("REST API Develmont with Spring")
               .beginEnrollmentDateTime(LocalDateTime.of(2019,11,30,20,4))
               .closeEnrollmentDateTime(LocalDateTime.of(2019,12,30,20,4))
               .beginEventDateTime(LocalDateTime.of(2019,12,30,20,4))
               .endEventDateTime(LocalDateTime.of(2019,12,30,20,4))
               .basePrice(100)
               .maxPrice(200)
               .limitOfEnrollment(100)
               .location("강남역 D2 스타텁 팩토")
               .free(true)   // 프리와 오프라인은 입력이 되면 안되는 값이다. 자동으로 계산되서 넣어저야하는 값
               .offline(false) //함
               .eventStatus(EventStatus.PUBLISHED)
               .build();

//이벤트 리파지솥리가 호출되면 이벤트객체를 리턴하라 !
       mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
       .contentType(MediaType.APPLICATION_JSON) //포스트 요청이 JSON을 보내고 있다.
       .accept(MediaTypes.HAL_JSON)//나는 어떤 응답을 원한다를 업셉트 헤더를 통해 알려줄 수 있다. - 나는 HAL JSON을 원한다.
       .content(objectMapper.writeValueAsString(event))) //accept 헤더를 통해 리퀘스트가 무엇을 원하는지 알 수 있다. //제이슨으로 바꾸고 본문에 넣어준 것
       .andDo(print())//콘솔에서 어떤 응답과 어떤 요청을 했는지 확인 할 수 있다.
       .andExpect(status().isCreated()) //201이 나오는지 확인해볼 것이다.
       .andExpect(jsonPath("id").exists()) //id 가 있는지 확인하는 테스트 구문
       .andExpect(header().exists(HttpHeaders.LOCATION))
       .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
       .andExpect(jsonPath("id").value(Matchers.not(100)))
       .andExpect(jsonPath("free").value(Matchers.not(true)))//이런 값들을 제한하기위해 DTO를 사용해서 막을 것
       .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
       ;

       //HAL 스펙을 이용해 밥고자 한다.

    }


}
