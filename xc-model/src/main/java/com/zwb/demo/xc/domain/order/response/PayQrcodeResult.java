package com.zwb.demo.xc.domain.order.response;

import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/** Created by mrt on 2018/3/27. */
@Data
@ToString
public class PayQrcodeResult extends ResponseResult {
    public PayQrcodeResult(ResultCode resultCode) {
        super(resultCode);
    }

    private String codeUrl;
    private Float money;
    private String orderNumber;
}
