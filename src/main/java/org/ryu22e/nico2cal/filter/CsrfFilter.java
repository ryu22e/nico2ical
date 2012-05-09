package org.ryu22e.nico2cal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ryu22e.nico2cal.exception.CsrfException;

/**
 * CSRF対策のためのフィルタークラス。
 * @author ryu22e
 *
 */
public final class CsrfFilter implements Filter {

    /**
     * @param request リクエスト
     * @return CSRF用トークンの検査に合格していればtrue, それ以外はfalse
     */
    private boolean hasCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String csrfTokenFromSession = session.getId();
        String csrfTokenFromParam = request.getParameter("csrftoken");
        return (csrfTokenFromSession != null && csrfTokenFromParam != null)
                && csrfTokenFromSession.equals(csrfTokenFromParam);
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
        String method = request.getMethod().toUpperCase();
        if (!"POST".equals(method) || !hasCsrfToken(request)) {
            throw new CsrfException();
        }

        chain.doFilter(request, response);
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
