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
class RestExporterWebInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext()
        rootCtx.register(ApplicationConfig)
        servletContext.addListener(new ContextLoaderListener(rootCtx))

        AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext()
        webCtx.register(WebConfig)

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webCtx)
        ServletRegistration.Dynamic reg = servletContext.addServlet("rest-exporter", dispatcherServlet)
        reg.setLoadOnStartup(1)
        reg.addMapping("/*")
    }
}