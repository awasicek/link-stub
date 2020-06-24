package net.wasicek.linkstub.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private Map<Integer, String> errorPageMap = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(500, "error-500"),
        new AbstractMap.SimpleEntry<>(400, "error-400"),
        new AbstractMap.SimpleEntry<>(404, "error-404"),
        new AbstractMap.SimpleEntry<>(410, "error-410")
    );

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String mvcViewName = "error";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            String errorPage = errorPageMap.get(statusCode);
            if(errorPage != null) mvcViewName = errorPage;
        }
        return mvcViewName;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
