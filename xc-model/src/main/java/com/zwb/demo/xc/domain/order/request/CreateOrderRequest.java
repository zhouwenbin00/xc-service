package com.zwb.demo.xc.domain.order.request;

import com.zwb.demo.xc.common.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/** Created by mrt on 2018/3/26. */
@Data
@ToString
public class CreateOrderRequest extends RequestData {

    String courseId;
}
