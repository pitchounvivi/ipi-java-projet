package com.myaudiolibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {


    //Affichage page d'Acceuil => http://localhost:5366
    @GetMapping(value = "/")
    public String index(){
        return "accueil";
    }

}
