package com.skc.os.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skc.os.api.common.Payment;
import com.skc.os.api.common.TransactionRequest;
import com.skc.os.api.common.TransactionResponse;
import com.skc.os.api.entity.Order;
import com.skc.os.api.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger log = LoggerFactory.getLogger(OrderService.class);

    public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
        String response= "";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        log.info("OrderService request : {}" + new ObjectMapper().writeValueAsString(request));

        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);
        //Payment paymentResponse = restTemplate.postForObject("ENDPOINT_URL", payment, Payment.class);

        log.info("Payment-service response from OrderService Rest call : {}" , new ObjectMapper().writeValueAsString(paymentResponse));

        response = paymentResponse.getPaymentStatus().equals("success")?"payment processing successful and order placed":"there is a failure in payment api, order added to cart.";


        orderRepository.save(order);

        return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
    }
}
