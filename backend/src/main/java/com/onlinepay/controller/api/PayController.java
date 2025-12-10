package com.onlinepay.controller.api;

import com.onlinepay.entity.OrderEntity;
import com.onlinepay.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final OrderService orderService;

    public PayController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建支付订单
     * @param order 订单信息
     * @return 支付订单信息
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order) {
        try {
            OrderEntity createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 查询支付订单
     * @param orderId 订单号
     * @return 订单信息
     */
    @PostMapping("/query")
    public ResponseEntity<?> queryOrder(@RequestBody OrderQueryRequest request) {
        try {
            OrderEntity order = orderService.queryOrder(request.getOrderId());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("查询订单失败：" + e.getMessage());
        }
    }

    /**
     * 关闭支付订单
     * @param request 订单号请求
     * @return 关闭结果
     */
    @PostMapping("/close")
    public ResponseEntity<?> closeOrder(@RequestBody OrderQueryRequest request) {
        try {
            OrderEntity order = orderService.closeOrder(request.getOrderId());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("关闭订单失败：" + e.getMessage());
        }
    }

    /**
     * 订单查询请求参数
     */
    private static class OrderQueryRequest {
        private String orderId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }

}