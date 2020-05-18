package com.netflix.eureka.resources;

import com.netflix.eureka.EurekaServerContextHolder;
import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * Jersey2 binder for the EurekaServerContext. Replaces the GuiceFilter in the server WAR web.xml
 *
 * @author Matt Nelson
 */
public class EurekaServerContextBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(() -> EurekaServerContextHolder.getInstance().getServerContext());
    }
}
