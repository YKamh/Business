package com.myself.business.model.recommand;

import com.myself.business.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by Kamh on 2018/6/4.
 */

public class RecommandHeadValue extends BaseModel{

    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<RecommandFooterValue> footer;

}
