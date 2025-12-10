package com.onlinepay.controller.api;

import com.onlinepay.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 异步通知控制器
 */
@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    private static final Logger log = LoggerFactory.getLogger(NotifyController.class);
    private final OrderService orderService;

    public NotifyController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 处理支付宝异步通知
     * @param request HTTP请求
     * @return 处理结果
     */
    @PostMapping("/alipay")
    public String alipayNotify(HttpServletRequest request) {
        try {
            // 获取所有请求参数
            Map<String, String> params = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);
                params.put(name, value);
            }

            log.info("收到支付宝异步通知：{}", params);

            // 处理通知
            boolean result = orderService.handleAlipayNotify(params);
            if (result) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            log.error("处理支付宝异步通知失败：{}", e.getMessage());
            return "fail";
        }
    }

}