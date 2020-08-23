package me.foodev.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events",produces = MediaTypes.HAL_JSON_VALUE) //미디어 타입에 해당되는 응답을 전달 할 것이다.
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper){
        this.eventRepository = eventRepository; //스프링 4.3부터는 생성자가 1개만있고 이 생성자로 받아올 파라미터가 이미 빈으로 등록되어 있다면
        //위 내용처럼 하나만 생성자를 사용하는 경우엔  autowired를 생략해도 된다.
        this.modelMapper = modelMapper;
    }

//컨트롤 R을 사용해 이전에 실행했던 테스트를 다시 실행 할 수 있다.
    @PostMapping
    public ResponseEntity create(@RequestBody EventDto eventDto){
        //Event event = EventDto.builder()
        // .name(eventDto.getName) 이런식으로 받아서 이벤트 객체로 만들어줘야하는데 모델 맵퍼라는 것을 활용해 간단하게 할 수 있다.
        // .build()
        Event event = modelMapper.map(eventDto,Event.class);//목킹시 객체 가 같지 않아서 에러남
        Event newEvent = this.eventRepository.save(event);
       URI createUri = linkTo(EventController.class).slash("{id}").toUri();
       //spring HATEOS가 제공하는 URI 생성 메소
     //  event.setId(10);
    return ResponseEntity.created(createUri).body(event);
    }
}
