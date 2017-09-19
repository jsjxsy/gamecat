package com.youximao.sdk.app.pay.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/11/18.
 */

public class CouponList implements Serializable {

    /**
     * list : [{"cc_no":"1111","cc_pname":"代金券","end_date":"2016-12-01","desc":"抵用描述"},{"cc_no":"1111","cc_pname":"代金券","end_date":"2016-12-01","desc":"抵用描述"}]
     * totalSize : 2
     */

    private int totalSize;
    private List<ListBean> list;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * cc_no : 1111
         * cc_pname : 代金券
         * end_date : 2016-12-01
         * desc : 抵用描述
         */

        private String cc_no;
        private String cc_pname;
        private String end_date;
        private String desc;

        public String getCc_no() {
            return cc_no;
        }

        public void setCc_no(String cc_no) {
            this.cc_no = cc_no;
        }

        public String getCc_pname() {
            return cc_pname;
        }

        public void setCc_pname(String cc_pname) {
            this.cc_pname = cc_pname;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
