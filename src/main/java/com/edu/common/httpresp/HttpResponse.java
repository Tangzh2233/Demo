package com.edu.common.httpresp;

/**
 * @author Tangzhihao
 * @date 2017/12/21
 */

public class HttpResponse {

    private  HttpRespError Error;
    private  HttpRespData Data;

    public HttpRespError getError() {
        return Error;
    }

    public void setError(HttpRespError error) {
        Error = error;
    }

    public HttpRespData getData() {
        return Data;
    }

    public void setData(HttpRespData data) {
        Data = data;
    }

    public static class HttpRespError{
        private String returnCode;
        private String returnMessage;

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }
    }

    public static class HttpRespData{
        private String version;
        private String responseTime;
        private String requestId;
        private String orderId;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(String responseTime) {
            this.responseTime = responseTime;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
