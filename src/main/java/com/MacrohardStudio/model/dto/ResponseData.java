package com.MacrohardStudio.model.dto;


import com.MacrohardStudio.model.enums.HttpStatusCode;

public class ResponseData<T>
{
    private Integer code;
    private T data;

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
}
