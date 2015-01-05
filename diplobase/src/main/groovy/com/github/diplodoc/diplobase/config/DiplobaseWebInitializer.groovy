package com.github.diplodoc.diplobase.config

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

/**
 * @author yaroslav.yermilov
 */
class DiplobaseWebInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext()
        rootContext.register(DiplobaseConfiguration)
        servletContext.addListener(new ContextLoaderListener(rootContext))

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext()
        webContext.register(WebConfiguration)

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext)
        ServletRegistration.Dynamic reg = servletContext.addServlet("rest-exporter", dispatcherServlet)
        reg.setLoadOnStartup(1)
        reg.addMapping("/*")
    }
}