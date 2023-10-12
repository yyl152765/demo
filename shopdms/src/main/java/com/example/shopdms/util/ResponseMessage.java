package com.example.shopdms.util;



public class ResponseMessage {
    private String resultCode;
    private String resultDesc;
    private Object obj;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void succeed() {
        setResultCode("000");
        setResultDesc("成功");
    }


    public ResponseMessage() {
        super();
        // TODO Auto-generated constructor stub
        setResultCode("001");
        setResultDesc("失败");
    }

}
