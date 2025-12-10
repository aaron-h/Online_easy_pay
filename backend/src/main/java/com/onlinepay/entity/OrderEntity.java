package com.onlinepay.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Entity
@Table(name = "\"order\"")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    // 订单状态常量
    public static final int SUCCESS = 3; // 订单成功
    public static final int PAID = 2; // 已支付
    public static final int WAIT = 1; // 等待支付
    public static final int CLOSE = -1; // 已关闭

    // 支付类型常量
    public static final int PAY_ALIPAY = 2; // 支付宝收款
    public static final int PAY_WECHAT_APP = 3; // 微信App收款
    public static final int PAY_ALIPAY_APP = 4; // 支付宝App收款

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer payType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal realPrice;

    @Column(nullable = false, length = 100)
    private String appName;

    @Column(nullable = false, length = 255)
    private String appItem;

    @Column(nullable = false)
    private Long appid;

    @Column(nullable = false, unique = true, length = 100)
    private String orderId;

    @Column(nullable = false, length = 255)
    private String notifyUrl;

    @Column(length = 255)
    private String returnUrl;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private LocalDateTime closeTime;

    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer state;

    @Column(columnDefinition = "text")
    private String param;

    @Column(name = "\"user\"")
    private String user;

    private String payImage;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppItem() {
        return appItem;
    }

    public void setAppItem(String appItem) {
        this.appItem = appItem;
    }

    public Long getAppid() {
        return appid;
    }

    public void setAppid(Long appid) {
        this.appid = appid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPayImage() {
        return payImage;
    }

    public void setPayImage(String payImage) {
        this.payImage = payImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}