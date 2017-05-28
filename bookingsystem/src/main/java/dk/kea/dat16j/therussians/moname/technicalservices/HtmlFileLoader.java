package dk.kea.dat16j.therussians.moname.technicalservices;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Chris on 17-May-17.
 */
public class HtmlFileLoader {
    public static void loadPage(HttpServletResponse response, String path) throws IOException {
        InputStream is = new FileInputStream(path);
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }
}
