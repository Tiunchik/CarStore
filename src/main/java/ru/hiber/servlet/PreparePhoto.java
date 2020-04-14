/**
 * Package ru.hiber.servlet for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hiber.model.Advertisement;
import ru.hiber.prog.Logic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Class PreparePhoto - download images for temp folder by post method and provide images bu get method
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 10.04.2020
 */
@WebServlet(urlPatterns = {"/images"})
public class PreparePhoto extends HttpServlet {

    private static final Logic LOGIC = Logic.getLogic();

    private static final Logger LOG = LogManager.getLogger(PreparePhoto.class.getName());

    private static final String TEMP_NAME = "temp";

    private static final String SENDING_IMAGE = "IO exception when send image";

    private static final String LOADING_IMAGE = "IO exception when load image";

    private static String path = "/images";


    @Override
    public void init() throws ServletException {
        super.init();
        path = getServletContext().getRealPath(path);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (OutputStream out = resp.getOutputStream()) {
            String imagename = req.getParameter("path");
            Advertisement adv = new Advertisement();
            adv.setId(Long.parseLong(imagename));
            byte[] image = LOGIC.getImage(adv);
            resp.setContentType("name=" + imagename);
            resp.setContentType("image/png");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + imagename + "\"");
            out.write(image);
            resp.setStatus(200);
        } catch (IOException io) {
            LOG.error(SENDING_IMAGE, io);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(directory);
            ServletFileUpload loadFile = new ServletFileUpload(factory);
            List<FileItem> fileList = loadFile.parseRequest(req);
            for (var e : fileList) {
                if (!e.isFormField()) {
                    File download = new File(directory + File.separator + TEMP_NAME);
                    try (FileOutputStream out = new FileOutputStream(download)) {
                        out.write(e.getInputStream().readAllBytes());
                        resp.setStatus(200);
                    }
                }
            }
        } catch (FileUploadException io) {
            LOG.error(LOADING_IMAGE, io);
        }
    }
}
