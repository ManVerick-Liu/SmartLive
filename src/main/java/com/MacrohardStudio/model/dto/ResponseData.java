package com.MacrohardStudio.model.dto;


import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.service.TokenService;

public class ResponseData<T>
{
    private Integer code;

    private T data;

    private String token;

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
