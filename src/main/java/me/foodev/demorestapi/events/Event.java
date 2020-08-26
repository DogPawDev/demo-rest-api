package me.foodev.demorestapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of ="id")
@Entity
public class Event {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT; //기본값으로 설정 해


    public void update(){
        //update free
        if(this.basePrice ==0 && this.maxPrice==0){
            this.free=true;
        }else{
            this.free=false;
        }

        //update offline
        if(this.location==null||this.location.isBlank()){
            this.offline=false;
        }//isBlank 자바 11부터 추가됨 조금더 확실한 체크
        else{
            this.offline=true;
        }
    }

}
