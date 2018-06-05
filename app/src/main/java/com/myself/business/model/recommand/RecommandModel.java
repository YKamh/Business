package com.myself.business.model.recommand;

import com.myself.business.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by Kamh on 2018/6/4.
 */

public class RecommandModel extends BaseModel{

    /**
     * 分别对应Json数据中的两个数据部分
     */
    public ArrayList<RecommandBodyValue> list;
    public RecommandHeadValue head;
}
