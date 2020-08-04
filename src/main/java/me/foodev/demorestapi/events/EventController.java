package me.foodev.demorestapi.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events",produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository){
        this.eventRepository = eventRepository; //스프링 4.3부터는 생성자가 1개만있고 이 생성자로 받아올 파라미터가 이미 빈으로 등록되어 있다면
        //autowired를 생략해도 된다.
    }


    @PostMapping
    public ResponseEntity create(@RequestBody Event event){
        Event newEvent = this.eventRepository.save(event);
       URI createUri = linkTo(EventController.class).slash("{id}").toUri();
       event.setId(10);
    return ResponseEntity.created(createUri).body(event);
    }
}
