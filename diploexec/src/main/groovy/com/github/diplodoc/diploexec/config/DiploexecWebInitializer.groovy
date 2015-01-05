package com.github.diplodoc.diploexec.config

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

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

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext()
        webContext.register(WebConfiguration)

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet('dispatcher', new DispatcherServlet(webContext))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping('/')
    }
}
