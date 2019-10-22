package com.zwb.demo.xc.common.model.response;

import lombok.Data;
import lombok.ToString;

/** Created by zwb on 2019/9/27 18:30 */
@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {
    QueryResult<T> queryResult;

    public QueryResponseResult(ResultCode resultCode, QueryResult<T> queryResult) {
        super(resultCode);
        this.queryResult = queryResult;
    }
}
