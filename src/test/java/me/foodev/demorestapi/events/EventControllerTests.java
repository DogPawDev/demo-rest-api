package me.foodev.demorestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.foodev.demorestapi.common.RestDocsConfiguration;
import me.foodev.demorestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest // 해당 어노테이션은 DemoRestApiApplication에서 빈을 다 읽어서 실행
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)//스프링 설정파일을 읽어와서 사용하는 방법
@ActiveProfiles("test") //h2를 사용하기 위한 설정 해당 에노테이션을 사용하면 테스트 applicaition-test.properties 를 사용
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
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
       EventDto event = EventDto.builder() //제대로 된 요청을 만들기 위해 사용
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
    //정상적인 값들이 들어오는 경우

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
               .andExpect(jsonPath("free").value(false))//이런 값들을 제한하기위해 DTO를 사용해서 막을 것
               .andExpect(jsonPath("offline").value(true))//이런 값들을 제한하기위해 DTO를 사용해서 막을 것
               .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
               .andExpect(jsonPath("_links.self").exists())
               .andExpect(jsonPath("_links.query-events").exists())
               .andExpect(jsonPath("_links.update-event").exists())
               .andDo(document("create-event",
                       links(
                               linkWithRel("self").description("link to self"),
                               linkWithRel("query-events").description("link to query events"),
                               linkWithRel("update-event").description("link to update an existing events"),
                               linkWithRel("profile").description("link to update an existing events")


                       ),
                       requestHeaders(
                               headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                       ),
                       requestFields(
                               fieldWithPath("name").description("Name of new Events"),
                               fieldWithPath("description").description("description of new events"),
                               fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                               fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                               fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                               fieldWithPath("endEventDateTime").description("date time of end of new event"),
                               fieldWithPath("location").description("location of new event"),
                               fieldWithPath("basePrice").description("basePrice of new event"),
                               fieldWithPath("maxPrice").description("maxPrice of new event"),
                               fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")




                               ),
                       responseHeaders(
                               headerWithName(HttpHeaders.LOCATION).description("locaiton header"),
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("contetn type")
                       ),
                       relaxedResponseFields( //문서의 일부분만 검증을 하도록 함
                               fieldWithPath("id").description("id of new Events"),
                               fieldWithPath("name").description("Name of new Events"),
                               fieldWithPath("description").description("description of new events"),
                               fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                               fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                               fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                               fieldWithPath("endEventDateTime").description("date time of end of new event"),
                               fieldWithPath("location").description("location of new event"),
                               fieldWithPath("basePrice").description("basePrice of new event"),
                               fieldWithPath("maxPrice").description("maxPrice of new event"),
                               fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                               fieldWithPath("free").description("it tells if this event is free event or not"),
                               fieldWithPath("offline").description("it tells if this event if offline event or not "),
                               fieldWithPath("eventStatus").description("event status")
                              // fieldWithPath("_links.self.herf").description("it tells if this event if offline event or not "),
                               //fieldWithPath("_links.query-events.herf").description("it tells if this event if offline event or not "),
                               //fieldWithPath("_links.q").description("it tells if this event if offline event or not ")
//                                       위와 같이 추가해주면 relaxed를 추가해 해당 검증을 안할 수 있다. 하지만 위 주석처럼 이렇게 해주는게 좋다                               )




               )))

       ;

    }
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Requset() throws Exception {
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
//들어와서는 안되는 값들이 들어오는 경우

        //bad 리퀘스트로 나오면 테스트 걸리는 경우
 mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                .contentType(MediaType.APPLICATION_JSON) //포스트 요청이 JSON을 보내고 있다.
                .accept(MediaTypes.HAL_JSON)//나는 어떤 응답을 원한다를 업셉트 헤더를 통해 알려줄 수 있다. - 나는 HAL JSON을 원한다.
                .content(objectMapper.writeValueAsString(event))) //accept 헤더를 통해 리퀘스트가 무엇을 원하는지 알 수 있다. //제이슨으로 바꾸고 본문에 넣어준 것
                .andDo(print())//콘솔에서 어떤 응답과 어떤 요청을 했는지 확인 할 수 있다.
                .andExpect(status().isBadRequest()) //201이 나오는지 확인해볼 것이다.
        ;

        //HAL 스펙을 이용해 밥고자 한다.

    }

   @Test
   @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build(); //입력값이 없을경우 201이 아니라 400이 나와야 정
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
   }

    @Test
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트") //주석을 달거나 에노테이션을 달아서 무슨 테스트인지 설명을 해놓으면 좋다
    //이런 에노테이션은 제이유닛5에서 설명 붙이는게 쉽다.
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("REST API Develmont with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,26,14,4))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,25,14,4))
                .beginEventDateTime(LocalDateTime.of(2019,12,30,20,4))
                .endEventDateTime(LocalDateTime.of(2019,12,30,20,4))
                .basePrice(10000)
                .maxPrice(200)     /// 끝나는 날보다 시작날이 더 뒤에 있거나 맥스 프라이스가 베이스 프라이스보다 값이 작은경우 등 제대로된 요청이 아님
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토")
                .build();

        //이런경우는 에노테이션으로 검증하기가 히믈다.
        //

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$[0].objectName").exists()json array는 언랩이 안됨
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
              //해당 값들이 있기를 바람
        //erros 변수안에 해당 값들이 들어 있다.
            ;

        //배드 리퀘스트 바디안에 메세지를 넣으려고 한다.
    }
}
