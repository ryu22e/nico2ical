package org.ryu22e.nico2cal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.util.AppEngineUtil;

/**
 * CookieにCSRF対策用トークンを設定するフィルタークラス。
 * @author ryu22e
 *
 */
public final class CsrfTokenCookieFilter implements Filter {

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * @param request リクエスト
     * @param response レスポンス
     * @param chain フィルターチェイン
     * @throws IOException
     * @throws ServletException
     */
    protected void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String csrfToken = request.getSession().getId();
        Cookie cookie = new Cookie("csrftoken", csrfToken);
        if (AppEngineUtil.isProduction()) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
        chain.doFilter(request, response);
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        doFilter(
            (HttpServletRequest) request,
            (HttpServletResponse) response,
            chain);
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

}
