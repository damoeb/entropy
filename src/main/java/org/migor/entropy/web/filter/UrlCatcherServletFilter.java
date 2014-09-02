package org.migor.entropy.web.filter;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.repository.ThreadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlCatcherServletFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(UrlCatcherServletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String requestUri = URLDecoder.decode(StringUtils.substring(httpRequest.getRequestURI(), 1), "UTF-8");
        if (isUrl(requestUri)) {

            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());

            ThreadRepository threadRepository = context.getBean(ThreadRepository.class);
            Thread thread = threadRepository.findByUri(requestUri);

            if (thread == null) {
                Document document = Jsoup.parse(new URL(requestUri), 400);
                httpResponse.sendRedirect(String.format("/#/threads/create?title=%s&uri=%s", URLEncoder.encode(document.title(), "UTF-8"), URLEncoder.encode(requestUri, "UTF-8")));
            } else {
                httpResponse.sendRedirect("/#/thread/" + thread.getId());
            }


        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isUrl(String requestURI) {

        if (!StringUtils.startsWithIgnoreCase(requestURI, "http")) {
            return false;
        }

        try {
            new URL(requestURI);
        } catch (MalformedURLException e) {
            return false;
        }

        return true;
    }
}
