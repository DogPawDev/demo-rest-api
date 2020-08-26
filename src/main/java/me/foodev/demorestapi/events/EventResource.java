package me.foodev.demorestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;


public class EventResource extends RepresentationModel {

    //이렇게 진행하면 출력 제이슨이 events 안에 필드들이 들어가 있따. 필드들이 그대로 묶이지 않고 진행하려면
    //JsonWnwrapped를 사용하면 된다.
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event){
        this.event=event;
    }

    public Event getEvent() {
        return event;
    }
}
