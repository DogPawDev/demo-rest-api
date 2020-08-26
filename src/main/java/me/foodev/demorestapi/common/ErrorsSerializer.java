package me.foodev.demorestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;
//오브젝트 매퍼에 등록을 해야하는데 제이슨 컴포넌틀르 사용하면 쉽게 등록이 가능하다.
// 오브젝트 매퍼가 에러스를 시리얼라이즈 할 때 사용한다.
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().forEach(e->{
            try{//json으로 만들어주는 부분이다.
                gen.writeStartObject();
                gen.writeStringField("field",e.getField());
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                Object rejectValue = e.getRejectedValue();
                if(rejectValue != null){
                    gen.writeStringField("rejectedValue",rejectValue.toString());
                }
                gen.writeEndObject();

            }catch (IOException e1){
                e1.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(e -> {
            try{
                gen.writeStartObject();
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                gen.writeEndObject();

            }catch (IOException e1){
                e1.printStackTrace();
            }
        });

        gen.writeEndArray();
        //errors에는 배열이 여러개 이므로 해당 메소드를 사용함
        //에러에는 필드 에러와 글로벌 에러가 있다.

    }
}
