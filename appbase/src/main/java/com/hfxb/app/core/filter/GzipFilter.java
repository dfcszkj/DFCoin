package com.hfxb.app.core.filter;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class GzipFilter implements Filter {
    /**
     * 过滤器配置
     */
    private FilterConfig filterConfig = null;

    /**
     * 构造方法
     * 
     * @return 过滤器配置
     */
    protected final FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        if (Constants.config.getBoolean("gzip",false)) {
            chain.doFilter(req, res);
        } else {
            if (req instanceof HttpServletRequest) {
            	// 排除不需要gzip的url
                if(!StringUtils.isInclude(((HttpServletRequest) req).getRequestURI(),
                						Constants.config.getStringArray("gzip.exclude"))) {
                    HttpServletRequest request = (HttpServletRequest) req;
                    HttpServletResponse response = (HttpServletResponse) res;
                    String ae = request.getHeader("accept-encoding");
                    if (ae != null && ae.indexOf("gzip") != -1) {
                        GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
                        chain.doFilter(req, wrappedResponse);
                        wrappedResponse.finishResponse();
                        return;
                    }
                }
            }
            chain.doFilter(req, res);
        }
    }
}