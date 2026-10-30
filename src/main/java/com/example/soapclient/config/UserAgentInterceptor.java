package com.example.soapclient.config;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.IOException;

/**
 * 요청에 User-Agent 헤더를 지정한다.
 * JDK HttpClient 기본값(Java-http-client/버전)을 차단하는 서버가 있어 애플리케이션 이름으로 바꿔 보낸다.
 */
public class UserAgentInterceptor implements ClientInterceptor {

    private final String userAgent;

    public UserAgentInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        WebServiceConnection connection = TransportContextHolder.getTransportContext().getConnection();
        if (connection instanceof HeadersAwareSenderWebServiceConnection headersAware) {
            try {
                headersAware.addRequestHeader("User-Agent", userAgent);
            } catch (IOException e) {
                throw new WebServiceIOException("User-Agent 헤더 추가 실패", e);
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) {
    }
}
