package com.github.diplodoc.diploexec.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

/**
 * @author yaroslav.yermilov
 */
class ConfigurationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    def Class<?>[] getRootConfigClasses() {
        [ BeanConfiguration.class ]
    }

    def Class<?>[] getServletConfigClasses() {
        [ WebConfiguration.class ]
    }

    def String[] getServletMappings() {
        [ '/' ]
    }
}