/**
 * Package ru.hiber.servlet for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hiber.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class AccessFilter - filter don't give access to pages that require to be logged user if you are not one
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 08.04.2020
 */
@WebFilter(urlPatterns = {"/userpage", "/create"})
public class AccessFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(AccessFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            servletRequest.getRequestDispatcher("/pages/mainList.html").forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
