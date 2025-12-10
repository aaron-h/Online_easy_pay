package com.onlinepay.service;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.onlinepay.config.AlipayConfig;
import com.onlinepay.entity.OrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * 支付宝服务类
 */
@Service
public class AlipayService {

    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);
    private final AlipayConfig alipayConfig;

    public AlipayService(AlipayConfig alipayConfig) {
        this.alipayConfig = alipayConfig;
    }

    /**
     * 初始化支付宝配置
     */
    @PostConstruct
    public void init() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = alipayConfig.getGatewayHost();
        config.signType = alipayConfig.getSignType();
        config.appId = alipayConfig.getAppId();
        config.merchantPrivateKey = alipayConfig.getMerchantPrivateKey();
        config.alipayPublicKey = alipayConfig.getAlipayPublicKey();
        config.notifyUrl = alipayConfig.getNotifyUrl();

        Factory.setOptions(config);
        log.info("支付宝配置初始化完成");
    }

    /**
     * 创建支付宝当面付订单
     * @param order 订单信息
     * @return 支付二维码链接
     * @throws Exception 异常信息
     */
    public String createFacetofaceOrder(OrderEntity order) throws Exception {
        // 模拟创建支付宝订单，返回模拟的二维码
        log.info("支付宝当面付订单创建成功，订单号：{}", order.getOrderId());
        return "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://www.alipay.com&orderId=" + order.getOrderId();
    }

    /**
     * 查询支付宝订单状态
     * @param orderId 订单号
     * @return 订单状态
     * @throws Exception 异常信息
     */
    public Object queryOrder(String orderId) throws Exception {
        // 模拟查询支付宝订单状态
        log.info("查询支付宝订单状态，订单号：{}", orderId);
        return new Object();
    }

    /**
     * 验证支付宝异步通知
     * @param params 通知参数
     * @return 是否验证通过
     */
    public boolean verifyNotify(Map<String, String> params) {
        try {
            return Factory.Payment.Common().verifyNotify(params);
        } catch (Exception e) {
            log.error("支付宝异步通知验证失败：{}", e.getMessage());
            return false;
        }
    }

}