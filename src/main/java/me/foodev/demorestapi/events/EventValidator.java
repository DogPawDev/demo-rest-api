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

        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
        ) {

            errors.rejectValue("endEventDateTime","wrongValue","endEventendTime wrong");
        }

        //다른 데이터 들도 이런 식으로 검증을 해야한다.
        //TODO beginEventDateTime
        //TODO CloseEnrollmentDateTime


    }
}
