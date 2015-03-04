package com.github.diplodoc.diplocore.config

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

/**
 * @author yaroslav.yermilov
 */
class DiplocoreWebInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext()
        rootContext.register(DiplocoreConfiguration)
        servletContext.addListener(new ContextLoaderListener(rootContext))

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext()
        webContext.register(DiplocoreWebConfiguration)

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet('diplocore-dispatcher', new DispatcherServlet(webContext))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping('/*')
    }
}
