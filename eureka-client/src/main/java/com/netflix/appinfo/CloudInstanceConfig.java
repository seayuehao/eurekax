/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.netflix.appinfo;

import com.google.inject.ProvidedBy;
import com.netflix.appinfo.providers.CloudInstanceConfigProvider;
import com.netflix.discovery.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * An {@link InstanceInfo} configuration for AWS cloud deployments.
 *
 * <p>
 * The information required for registration with eureka by a combination of
 * user-supplied values as well as querying AWS instance metadata.An utility
 * class {@link  } helps in retrieving AWS specific values. Some of
 * that information including <em>availability zone</em> is used for determining
 * which eureka server to communicate to.
 * </p>
 *
 * @author Karthik Ranganathan
 */
@Singleton
@ProvidedBy(CloudInstanceConfigProvider.class)
public class CloudInstanceConfig extends PropertiesInstanceConfig implements RefreshableInstanceConfig {
    private static final Logger logger = LoggerFactory.getLogger(CloudInstanceConfig.class);

    private static final String[] DEFAULT_AWS_ADDRESS_RESOLUTION_ORDER = new String[]{
//            MetaDataKey.publicHostname.name(),
//            MetaDataKey.localIpv4.name()
    };

    public CloudInstanceConfig() {
        this(CommonConstants.DEFAULT_CONFIG_NAMESPACE);
    }

    public CloudInstanceConfig(String namespace) {
        super(namespace);
//        this(namespace, new Archaius1AmazonInfoConfig(namespace), null, true);
    }


    /**
     * @deprecated use {@link #resolveDefaultAddress(boolean)}
     */
    @Deprecated
    public String resolveDefaultAddress() {
        return this.resolveDefaultAddress(true);
    }

    @Override
    public String resolveDefaultAddress(boolean refresh) {
        // In this method invocation data center info will be refreshed.
        String result = getHostName(refresh);

        for (String name : getDefaultAddressResolutionOrder()) {
            try {

            } catch (Exception e) {
                logger.error("failed to resolve default address for key {}, skipping", name, e);
            }
        }

        return result;
    }

    @Override
    public String getHostName(boolean refresh) {
        return null;
    }

    @Override
    public String getIpAddress() {
        return this.shouldBroadcastPublicIpv4Addr() ? getPublicIpv4Addr() : getPrivateIpv4Addr();
    }

    private String getPrivateIpv4Addr() {
        return null;
    }

    private String getPublicIpv4Addr() {
        return null;
    }

    @Override
    public DataCenterInfo getDataCenterInfo() {
        return null;
    }

    @Override
    public String[] getDefaultAddressResolutionOrder() {
        String[] order = super.getDefaultAddressResolutionOrder();
        return (order.length == 0) ? DEFAULT_AWS_ADDRESS_RESOLUTION_ORDER : order;
    }

}
