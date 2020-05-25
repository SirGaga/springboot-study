package com.zhangjie.springbootstudy.controller;

import com.zhangjie.springbootstudy.domain.Person;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MyController {
    @RequestMapping(value = "/person",method = RequestMethod.GET)
    public Person getPerson(){
        Person person = new Person();
        person.setId(15);
        person.setName("张洁");
        person.setBirthday(new Date());
        return person;
    }

}
