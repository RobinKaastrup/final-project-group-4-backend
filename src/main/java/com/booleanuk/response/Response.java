package com.booleanuk.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Response<T> {
    protected String status;
    protected T data;

    public void set(T data){
        this.status = "success";
        this.data = data;
    }
}
