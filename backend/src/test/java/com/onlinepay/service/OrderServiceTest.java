package com.onlinepay.service;

import com.onlinepay.entity.OrderEntity;
import com.onlinepay.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 订单服务测试类
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AlipayService alipayService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() throws Exception {
        // 准备测试数据
        OrderEntity order = new OrderEntity();
        order.setPayType(OrderEntity.PAY_ALIPAY);
        order.setPrice(new BigDecimal(100.00));
        order.setRealPrice(new BigDecimal(100.00));
        order.setAppName("测试应用");
        order.setAppItem("测试商品");
        order.setAppid(1L);
        order.setNotifyUrl("http://localhost/notify");
        order.setCreateTime(LocalDateTime.now());

        // 模拟支付宝服务返回支付二维码
        String qrCode = "https://example.com/qrcode.png";
        when(alipayService.createFacetofaceOrder(any(OrderEntity.class))).thenReturn(qrCode);

        // 模拟订单保存
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        // 执行测试
        OrderEntity createdOrder = orderService.createOrder(order);

        // 验证结果
        assertNotNull(createdOrder);
        assertEquals(OrderEntity.WAIT, createdOrder.getState());
        assertEquals(qrCode, createdOrder.getPayImage());
        verify(alipayService, times(1)).createFacetofaceOrder(any(OrderEntity.class));
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testQueryOrder() {
        // 准备测试数据
        String orderId = "test_order_id";
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setOrderId(orderId);

        // 模拟订单查询
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));

        // 执行测试
        OrderEntity foundOrder = orderService.queryOrder(orderId);

        // 验证结果
        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getOrderId());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void testQueryOrderNotFound() {
        // 准备测试数据
        String orderId = "not_exist_order_id";

        // 模拟订单不存在
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.queryOrder(orderId);
        });
        assertEquals("订单不存在", exception.getMessage());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void testCloseOrder() {
        // 准备测试数据
        String orderId = "test_order_id";
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setOrderId(orderId);
        order.setState(OrderEntity.WAIT);

        // 模拟订单查询
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));
        // 模拟订单保存
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);

        // 执行测试
        OrderEntity closedOrder = orderService.closeOrder(orderId);

        // 验证结果
        assertNotNull(closedOrder);
        assertEquals(OrderEntity.CLOSE, closedOrder.getState());
        assertNotNull(closedOrder.getCloseTime());
        verify(orderRepository, times(1)).findByOrderId(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCloseOrderInvalidState() {
        // 准备测试数据
        String orderId = "test_order_id";
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setOrderId(orderId);
        order.setState(OrderEntity.PAID);

        // 模拟订单查询
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.closeOrder(orderId);
        });
        assertEquals("订单状态不正确，无法关闭", exception.getMessage());
        verify(orderRepository, times(1)).findByOrderId(orderId);
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

}