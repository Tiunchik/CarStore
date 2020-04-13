/**
 * Package ru.hiber.servlet for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class PageServlet - Page servlet, provide access for all programm pages by URL
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 08.04.2020
 */
@WebServlet(urlPatterns = {"/main", "/userpage", "/reg", "/create", "/car"})
public class PageServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(PageServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = req.getContextPath();
        s = req.getServletPath();
        if (req.getServletPath().contains("main")) {
            req.getRequestDispatcher("/pages/mainList.html").forward(req, resp);
        }
        if (req.getServletPath().contains("userpage")) {
            req.getRequestDispatcher("/pages/userPage.html").forward(req, resp);
        }
        if (req.getServletPath().contains("reg")) {
            req.getRequestDispatcher("/pages/register.html").forward(req, resp);
        }
        if (req.getServletPath().contains("create")) {
            req.getRequestDispatcher("/pages/createAD.html").forward(req, resp);
        }
        if (req.getServletPath().contains("car")) {
            req.getRequestDispatcher("/pages/carPage.html").forward(req, resp);
        }
    }
}
