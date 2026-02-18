package springcourse.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.swing.*;
import java.nio.charset.StandardCharsets;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerEncodingFilter(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    private void registerEncodingFilter(ServletContext context) {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        context.addFilter("characterEncodingFilter", filter)
                .addMappingForUrlPatterns(null, true, "/*");
    }

    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }
}
