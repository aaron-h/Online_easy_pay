package com.onlinepay.service;

import com.onlinepay.entity.OrderEntity;
import com.onlinepay.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 订单服务类
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final AlipayService alipayService;

    public OrderService(OrderRepository orderRepository, AlipayService alipayService) {
        this.orderRepository = orderRepository;
        this.alipayService = alipayService;
    }

    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建的订单
     * @throws Exception 异常信息
     */
    public OrderEntity createOrder(OrderEntity order) throws Exception {
        // 生成唯一订单号
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        // 设置订单状态为等待支付
        order.setState(OrderEntity.WAIT);
        // 设置创建时间
        order.setCreateTime(LocalDateTime.now());
        // 创建支付宝订单并获取支付二维码
        String qrCode = alipayService.createFacetofaceOrder(order);
        order.setPayImage(qrCode);
        // 保存订单
        return orderRepository.save(order);
    }

    /**
     * 查询订单
     * @param orderId 订单号
     * @return 订单信息
     */
    public OrderEntity queryOrder(String orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    /**
     * 关闭订单
     * @param orderId 订单号
     * @return 关闭的订单
     */
    public OrderEntity closeOrder(String orderId) {
        OrderEntity order = queryOrder(orderId);
        if (order.getState() != OrderEntity.WAIT) {
            throw new RuntimeException("订单状态不正确，无法关闭");
        }
        // 设置订单状态为已关闭
        order.setState(OrderEntity.CLOSE);
        order.setCloseTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 处理支付宝异步通知
     * @param params 支付宝通知参数
     * @return 处理结果
     */
    public boolean handleAlipayNotify(Map<String, String> params) {
        // 验证通知
        if (!alipayService.verifyNotify(params)) {
            return false;
        }
        // 获取订单号
        String orderId = params.get("out_trade_no");
        OrderEntity order = queryOrder(orderId);
        // 检查订单状态
        if (order.getState() != OrderEntity.WAIT) {
            return false;
        }
        // 更新订单状态
        order.setState(OrderEntity.PAID);
        order.setPayTime(LocalDateTime.now());
        orderRepository.save(order);
        return true;
    }
}