package com.rookiefly.test.commons.redis;

import java.io.Serializable;

public class PublishMessage implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5107362145475526932L;

    /**
     * 原始消息
     */
    private String message;

    /**
     * 消息发布时间
     */
    private long publishTime;

    /**
     * 应用上下文，目前就flag
     */
    private String flag;

    public PublishMessage() {
    }

    public PublishMessage(String message) {
        this.message = message;
        publishTime = System.currentTimeMillis();
    }

    public PublishMessage genMessageContext() {
        PublishMessage msg = new PublishMessage();
        msg.setFlag(flag);
        msg.setPublishTime(publishTime);
        return msg;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "PublishMessage [message=" + message + ", publishTime=" + publishTime + ", flag=" + flag + "]";
    }

    /**
     * Getter method for property <tt>message</tt>.
     *
     * @return property value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for property <tt>message</tt>.
     *
     * @param message value to be assigned to property message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter method for property <tt>publishTime</tt>.
     *
     * @return property value of publishTime
     */
    public long getPublishTime() {
        return publishTime;
    }

    /**
     * Setter method for property <tt>publishTime</tt>.
     *
     * @param publishTime value to be assigned to property publishTime
     */
    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * Getter method for property <tt>flag</tt>.
     *
     * @return property value of flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Setter method for property <tt>flag</tt>.
     *
     * @param flag value to be assigned to property flag
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }
}
