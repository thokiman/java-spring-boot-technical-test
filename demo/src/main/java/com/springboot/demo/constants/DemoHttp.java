package com.springboot.demo.constants;

public class DemoHttp {
    // https://api.github.com/search/users?q=kafka&sort=followers&order=desc&per_page=100&page=1
    public static final String BASE_URL = "https://api.github.com/search/users";
    public static final String QS_QUERY="q=";
    public static final String QS_SORT ="sort=";
    public static final String QS_ORDER = "order=";
    public static final String QS_PER_PAGE = "per_page=";
    public static final String QS_PAGE="page=";

    public static final String DEFAULT_SORT = "followers";
    public static final String DEFAULT_ORDER = "asc";
    public static final int DEFAULT_PER_PAGE = 100;
    public static final int DEFAULT_PAGE = 1;

    public static final String OPTIONAL_SORT_FOLLOWERS = "followers";
    public static final String OPTIONAL_SORT_REPOSITORIES = "repositories";
    public static final String OPTIONAL_SORT_JOINED = "joined";

    public static final String OPTIONAL_ORDER_ASC = "asc";
    public static final String OPTIONAL_ORDER_DESC = "desc";
}

