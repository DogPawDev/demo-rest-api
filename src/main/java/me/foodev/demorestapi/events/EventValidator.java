package me.foodev.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void  validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice","wrongValue","BasePrice is Wrong");
            errors.rejectValue("MaxPrice","wrongValue","BasePrice is Wrong");
            errors.reject("wrongPricces","Price are wrong");
            //리젝트 밸류로 에러를 넣을 경우 필드 영역에 에러가 들어가고
            //리젝트로 넣을경우 에러의 글로벌 영역에 삽입된다.
        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
        ) {

            errors.rejectValue("endEventDateTime","wrongValue","endEventend Time wrong");
        }

        //다른 데이터 들도 이런 식으로 검증을 해야한다.
        //TODO beginEventDateTime
        //TODO CloseEnrollmentDateTime


    }
}
