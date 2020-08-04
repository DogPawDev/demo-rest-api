package me.foodev.demorestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest //웹과 관련된 빈들이 모두 등록이 된다. 웹과 관련된 부분만 태스트하기 때문에 속도가 빠르다.
//하지만 단위 테스트라고 보기에는 어렵다. 디스패처서블릿 안에 다양한 것들이 이미 포함되어 작동하기 때문에
//슬라이싱 테스트
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 요청을 만들 수 있고 응답을 검증할 수 있다.
    //스픨ㅇ mvc test에서 가장 핵심인 테스트 어노테이션
    //웹서버를 뛰우지 않기 때문에 조금 빠르지만 디스패처 서블릿을 만들어야 하기 때문에 단위 테스트 보단 느림
    //만드는 객체도 많고 초반 구동이 약간 딜레이가 있다.
    // 단위테스트 보다는 좀 더 걸리고 웹서버를 띄우는것 보단 빠른?리

    @Autowired
    ObjectMapper objectMapper;
    //스프링 부트에 이미 been으로 등록이 되어 있기 대문에 쉽게 사용이 가능하다.

    @Test
    public void createEvent() throws Exception {
       Event event = Event.builder()
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
               .build();


       mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
       .contentType(MediaType.APPLICATION_JSON)
       .accept(MediaTypes.HAL_JSON)
       .content(objectMapper.writeValueAsString(event))) //accept 헤더를 통해 리퀘스트가 무엇을 원하는지 알 수 있다.
       .andDo(print())
       .andExpect(status().isCreated())
       .andExpect(jsonPath("id").exists())
       ;

       //HAL 스펙을 이용해 밥고자 한다.

    }
}
