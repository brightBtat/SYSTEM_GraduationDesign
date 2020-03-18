package com.cys.search.filter;

import com.cys.search.feign.SystemInterface;
import com.cys.search.pojo.AuthUrl;
import com.cys.search.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
@Order(0)
public class TokenFilter implements HandlerInterceptor {

    @Autowired
    private SSOService ssoService;

    @Autowired
    private SystemInterface systemInterface;

    protected String loginUrl = "http://www.cys.com:9200/sso/sso/html/login.html";

    protected final static String COOKIENAME="SYS-TOKEN";

    private final static List<AuthUrl> authUrlList = new LinkedList<>();

//    @PostConstruct
//    public void getAllAuthUrl(){
//        List<AuthUrl> authUrl = systemInterface.getAllAuthUrl();
//        authUrlList.addAll(authUrl);
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        StringBuffer requestURL = request.getRequestURL();

        //权限路径白名单检测排除
        if(!authUrlList.isEmpty() && authUrlList.toString().contains(requestURL)){
            return true;
        }

        Map<String,Object> userMap = ssoService.getUser(request);
        if(userMap == null || userMap.isEmpty()){
            response.sendRedirect(loginUrl);
            return false;
        }
        return true;
    }
}
