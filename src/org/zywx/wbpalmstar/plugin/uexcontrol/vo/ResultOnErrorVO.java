package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class ResultOnErrorVO implements Serializable{
    private static final long serialVersionUID = 3159884800110901437L;
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
