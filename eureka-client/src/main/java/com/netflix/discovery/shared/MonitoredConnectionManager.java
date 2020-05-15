package com.netflix.discovery.shared;

import javax.net.ssl.SSLSocketFactory;

/**
 * connection reuse statistics, as its underlying connection pool.
 *
 * @author awang
 *
 */
public class MonitoredConnectionManager {

    SSLSocketFactory sslSocketFactory;
    public MonitoredConnectionManager(String name) {
        super();
        initMonitors(name);
    }
//
//    public MonitoredConnectionManager(String name, SchemeRegistry schreg, long connTTL,
//                                      TimeUnit connTTLTimeUnit) {
//        initMonitors(name);
//    }

    public MonitoredConnectionManager(String name, SSLSocketFactory schreg) {
        initMonitors(name);
        sslSocketFactory = schreg;
    }

    void initMonitors(String name) {

    }

//    @Override
//    @Deprecated
//    protected AbstractConnPool createConnectionPool(HttpParams params) {
//        return new NamedConnectionPool(connOperator, params);
//    }
//
//    @Override
//    protected ConnPoolByRoute createConnectionPool(long connTTL,
//                                                   TimeUnit connTTLTimeUnit) {
//        return new NamedConnectionPool(connOperator, connPerRoute, 20, connTTL, connTTLTimeUnit);
//    }
//
//    @VisibleForTesting
//    ConnPoolByRoute getConnectionPool() {
//        return this.pool;
//    }
//
//    @Override
//    public ClientConnectionRequest requestConnection(HttpRoute route,
//                                                     Object state) {
//        // TODO Auto-generated method stub
//        return super.requestConnection(route, state);
//    }

}
