package com.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/cars")  //http://localhost:8080/api/v1/cars/
public class CarsController {

    @PostMapping("/add-car")
    public String addCars(){
        return "Add cars here";
    }


}
