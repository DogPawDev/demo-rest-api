package me.foodev.demorestapi.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto {
    //입력 값을 받는 DTO를 밖으로 빼서 코드 가독성을 높임 하지만 중복이 있을 수 있다.
    //이만큼만 입력을 받을 수 잇다.
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
}
