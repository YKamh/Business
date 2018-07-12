package com.myself.business.model.course;

import com.myself.business.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by Kamh on 2018/7/12.
 */

public class BaseCourseModel extends BaseModel {

    public String ecode;
    public String emsg;
    public CourseModel data;

    public CourseHeaderValue head;
    public CourseFooterValue footer;
    public ArrayList<CourseCommentValue> body;
}
