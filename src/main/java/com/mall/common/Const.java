package com.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "current_user";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public static final String TOKEN = "token_";

    public interface ProdectListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc","price_desc");
    }

    public interface checkedProduct{
        int CHECKED = 1;//购物车选中状态
        int UN_CHECKED = 0;//购物车没有被选中状态

        String LIMIT_QUANTITY_SUCCESS = "LIMIT_QUANTITY_SUCCESS";
        String LIMIT_QUANTITY_FAIL = "LIMIT_QUANTITY_FAIL";
    }

    public interface role{
        int ROLE_CUSTOMER = 0;
        int ROLE_MAIN = 1;
    }

    public enum ProductStatus{
        ON_STATUS("在线",1);
        private String value;
        private int code;

        ProductStatus(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
