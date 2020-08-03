package me.foodev.demorestapi.events;


import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Spring REST API")
                .description("RESt APi DEVELOPMENt WIHT SPRING")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //자바빈 스펙을 이용해서도 만들 수 있어야 한다.

        String description = "spring";
        String name = "Event";

        Event event = new Event();
        //기본생성자가 없으면 만들어지지 않기 때문에 Event에서 @AllArgsConstructor @NoArgsConstructor를 추개해야 한다.

        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description); // alt+command+v

        //태스트 실행 단축기는 controll+shft+r

    }

}