package com.github.diplodoc.diploexec.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.EnableWebMvc

import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

/**
 * @author yaroslav.yermilov
 */
class DiploexecWebInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext()
        rootContext.register(DiploexecConfiguration)
        servletContext.addListener(new ContextLoaderListener(rootContext))

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet())
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")
    }
}
