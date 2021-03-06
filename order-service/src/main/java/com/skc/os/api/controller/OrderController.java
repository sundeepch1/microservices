package com.skc.os.api.controller;

import com.skc.os.api.common.Payment;
import com.skc.os.api.common.TransactionRequest;
import com.skc.os.api.common.TransactionResponse;
import com.skc.os.api.entity.Order;
import com.skc.os.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transaction;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request){
        TransactionResponse response = null;
        try {
            response  = orderService.saveOrder(request);
        }catch (Exception e){
            System.out.println("Error occurred try later");
        }

        return response;
    }

}
