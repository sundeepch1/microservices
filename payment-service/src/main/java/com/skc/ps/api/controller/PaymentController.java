package com.skc.ps.api.controller;

import com.skc.ps.api.entity.Payment;
import com.skc.ps.api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    public Payment doPayment(@RequestBody Payment payment){

        Payment paymentResponse = null;
        try {
            paymentResponse = paymentService.doPayment(payment);
        }catch (Exception e){

        }

        return paymentResponse;
    }

    @GetMapping("/{orderId}")
    public Payment findPaymentHistoryByOrderId(@PathVariable int orderId){
        Payment payment = null;
        try {
            payment = paymentService.findPaymentHistoryByOrderId(orderId);
        }catch (Exception e){
            System.out.println("Some error occurred try later");
        }

        return payment;
    }


}
