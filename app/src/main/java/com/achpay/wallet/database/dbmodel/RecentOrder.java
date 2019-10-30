package com.achpay.wallet.database.dbmodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RecentOrder {

    @Id
    private String orderId;
    private String orderTime;
    private String orderStatus;
    private String orderDetail;
    private boolean isRead;
    private boolean isDialogShown;
    private boolean isPrinted;
    @Generated(hash = 921899191)
    public RecentOrder(String orderId, String orderTime, String orderStatus,
            String orderDetail, boolean isRead, boolean isDialogShown,
            boolean isPrinted) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderDetail = orderDetail;
        this.isRead = isRead;
        this.isDialogShown = isDialogShown;
        this.isPrinted = isPrinted;
    }
    @Generated(hash = 919723832)
    public RecentOrder() {
    }
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderTime() {
        return this.orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    public String getOrderStatus() {
        return this.orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderDetail() {
        return this.orderDetail;
    }
    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public boolean getIsPrinted() {
        return this.isPrinted;
    }
    public void setIsPrinted(boolean isPrinted) {
        this.isPrinted = isPrinted;
    }
    public boolean getIsDialogShown() {
        return this.isDialogShown;
    }
    public void setIsDialogShown(boolean isDialogShown) {
        this.isDialogShown = isDialogShown;
    }
    

}
