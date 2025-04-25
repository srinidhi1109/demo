package com.example.mutualfollowers.model;

public class WebhookResponse {
    private String webhook;
    private String accessToken;
    private DataWrapper data;

    public WebhookResponse() {
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public DataWrapper getData() {
        return data;
    }

    public void setData(DataWrapper data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebhookResponse{" +
                "webhook='" + webhook + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", data=" + data +
                '}';
    }
}