package net.nanquanyuhao.security.distributed.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import net.nanquanyuhao.security.distributed.gateway.common.EncryptUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 权限拦截器
 * 实际在权限拦截器起作用之前，oauth框架已经拿到 jwt 做好了响应的解码处理，所以上下文中才可以取到 authentication
 */
public class AuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        // 查看下 requset 的内容，此处会拿到完整的请求头就是传递的 jwt 的值
        System.out.println("jwt 内容:" + context.getRequest().getHeader("Authorization"));

        // 从安全上下文中获取用户身份对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 无 token 访问网关内资源的情况，目前仅有 uua 服务直接暴露
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        // 取出用户身份信息
        String principal = userAuthentication.getName();

        // 获取当前用户权限信息
        List<String> authorities = new ArrayList<>();
        userAuthentication.getAuthorities().stream().forEach(
                c -> authorities.add(((GrantedAuthority) c).getAuthority()));

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if (userAuthentication != null) {
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);
        }

        // 把身份信息和权限信息放在 json 中，加入 http 的 header 中
        context.addZuulRequestHeader("json-token",
                EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        return null;
    }
}
