package me.foodev.demorestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(JUnitParamsRunner.class)

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

    private Object[] paramsForTestFree(){
        return new Object[]{
                new Object[]{0,0,true},
                new Object[]{100,0,false},
                new Object[]{0,100,false},
                new Object[]{100,200,false},
        };
    }

    @Test
    /*
    @Parameters({
            "0,0,true",
            "100,0,false",
            "0,100,false"
    })//해당 값들이 파라미터로 들어간다. 문자열로 써도되고
    //오브젝트의 메소드를 주어 사용도 가능하다
    */
    @Parameters(method = "paramsForTestFree")
    public void testFree(int basePrice,int maxPrice,boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);



    }

    private Object[] paramsForTestOffline(){
        return new Object[]{
                new Object[]{"강남",true},
                new Object[]{null,false},
                new Object[]{"",false}


        };
    }

    @Test
    @Parameters(method = "paramsForTestOffline")
    public void testOffLIne(String location , boolean isOffline){
        //given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

    }
}