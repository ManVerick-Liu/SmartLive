package com.MacrohardStudio.model.enums;

public enum WebSocketStatusCode
{
    // 信息性状态码
    CONTINUE(100),
    SWITCHING_PROTOCOLS(101),

    // 成功状态码
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),

    // 重定向状态码
    MOVED_PERMANENTLY(301),
    FOUND(302),
    NOT_MODIFIED(304),

    // 客户端错误状态码
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),

    // 服务器错误状态码
    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    private final Integer value;

    WebSocketStatusCode(Integer value)
    {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
