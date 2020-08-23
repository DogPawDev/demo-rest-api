package me.foodev.demorestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRestApiApplication.class, args);
    }


//모델 매퍼는 공용으로 사용할 수 잇기 때문에 빈으로 등록해서 사용한다.
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }


}
