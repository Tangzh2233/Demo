package com.edu.JavaLearning.Netty.longpolling;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import javax.mail.MethodNotSupportedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/11 6:42 PM
 **/
public class HttpParamParse {
    private HttpRequest httpRequest;

    public HttpParamParse(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public static Map<String, String> parse(HttpRequest request) throws IOException, MethodNotSupportedException {
        Map<String, String> param = new HashMap<>();
        if (request.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters()
                    .forEach((key, value) -> param.put(key, value.get(0)));
        } else if (request.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
                Attribute item = (Attribute) data;
                param.put(item.getName(), item.getValue());
            }
        } else {
            throw new MethodNotSupportedException("");
        }
        return param;
    }
}
