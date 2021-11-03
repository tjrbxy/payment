package com.kjxls.payment.controller;

import com.kjxls.payment.entity.Payment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public Object index(Model model){

        Payment payment = new Payment("1112232", "1112232");
        model.addAttribute("payment",payment);

        return "index";
    }
}
