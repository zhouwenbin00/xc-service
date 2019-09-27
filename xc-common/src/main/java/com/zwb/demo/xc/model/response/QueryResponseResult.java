package com.zwb.demo.xc.model.response;

import lombok.Data;
import lombok.ToString;

/** Created by zwb on 2019/9/27 18:30 */
@Data
@ToString
public class QueryResponseResult extends ResponseResult {
    QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode, QueryResult queryResult) {
        super(resultCode);
        this.queryResult = queryResult;
    }
}
