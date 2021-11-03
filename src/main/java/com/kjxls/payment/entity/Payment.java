package com.kjxls.payment.entity;

import lombok.Data;

import javax.swing.*;

@Data
public class Payment {

    private String name;
    private String pay;

    public Payment(String name, String pay) {
        this.name = name;
        this.pay = pay;
    }
}
