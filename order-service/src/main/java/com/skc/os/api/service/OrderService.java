package com.skc.os.api.service;

import com.skc.os.api.common.Payment;
import com.skc.os.api.common.TransactionRequest;
import com.skc.os.api.common.TransactionResponse;
import com.skc.os.api.entity.Order;
import com.skc.os.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy
    RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri:http://PAYMENT-SERVICE/payment/doPayment/}")
    private String ENDPOINT_URL;

    public TransactionResponse saveOrder(TransactionRequest request){
        String response= "";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);
        //Payment paymentResponse = restTemplate.postForObject("ENDPOINT_URL", payment, Payment.class);
        response = paymentResponse.getPaymentStatus().equals("success")?"payment processing successful and order placed":"there is a failure in payment api, order added to cart.";


        orderRepository.save(order);

        return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
    }
}
