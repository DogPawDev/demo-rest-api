package me.foodev.demorestapi.index;


import me.foodev.demorestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {


    /*
        ResourceSupport is now RepresentationModel

        Resource is now EntityModel

        Resources is now CollectionModel

        PagedResources is now PagedModel
        HATEOS 버전 변경으로 다음과 같이 변경됨
     */

    @GetMapping("/api")
    public RepresentationModel index(){
         var index = new RepresentationModel();
         index.add(linkTo(EventController.class).withRel("events"));
         return index;
    }

}
