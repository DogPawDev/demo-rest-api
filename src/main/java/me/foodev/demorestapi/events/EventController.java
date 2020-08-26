package me.foodev.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events",produces = MediaTypes.HAL_JSON_VALUE) //미디어 타입에 해당되는 응답을 전달 할 것이다.
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper,EventValidator eventValidator){
        this.eventRepository = eventRepository; //스프링 4.3부터는 생성자가 1개만있고 이 생성자로 받아올 파라미터가 이미 빈으로 등록되어 있다면
        //위 내용처럼 하나만 생성자를 사용하는 경우엔  autowired를 생략해도 된다.
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

//컨트롤 R을 사용해 이전에 실행했던 테스트를 다시 실행 할 수 있다.
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto, Errors errors){
        //vaild를 이용해 검증을 수행할 수 있다. EventDto에 값들을 설정 할 수 있다.
        //valid 어노테이션을 붙였기 때문에 제이슨과 바인딩을 할 때 해당 어노테이션을 활용한다. ex)Notnull 등등 검증을 수행함
        //검증을 한 결과를 @valid 어노테이션을 사용한 객체 바로 오른쪽에 있는 Erros 객체에다가 넣어준다.

        //Event event = EventDto.builder()
        // .name(eventDto.getName) 이런식으로 받아서 이벤트 객체로 만들어줘야하는데 모델 맵퍼라는 것을 활용해 간단하게 할 수 있다.
        // .build()

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        eventValidator.validate(eventDto,errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
            //errors 객체를 JSON으로 변환 할 수 없어서 바디에 바로 넣어서 못 보낸다.
            //자바빈 스펙을 준수하지 않으므로
            //Event 객체는 자바빈 스펙을 준수해서 자동으로 변환이 된다.
        }

        Event event = modelMapper.map(eventDto,Event.class);//목킹시 객체 가 같지 않아서 에러남
        event.update();
        Event newEvent = this.eventRepository.save(event);


        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createUri = selfLinkBuilder.toUri(); //리퀘스트매핑에 적힌 URI를 가저와 거기다 붙인 것
       //spring HATEOS가 제공하는 URI 생성 메소
     //  event.setId(10);
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));


        return ResponseEntity.created(createUri).body(eventResource);
    }
}
