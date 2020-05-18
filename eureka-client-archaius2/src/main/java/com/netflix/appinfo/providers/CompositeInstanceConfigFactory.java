package com.netflix.appinfo.providers;

import com.netflix.appinfo.EurekaArchaius2InstanceConfig;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.annotations.ConfigurationSource;
import com.netflix.discovery.CommonConstants;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.internal.util.InternalPrefixedConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A factory for {@link com.netflix.appinfo.EurekaInstanceConfig} that can provide either
 * {@link com.netflix.appinfo.Ec2EurekaArchaius2InstanceConfig} or
 * {@link com.netflix.appinfo.EurekaArchaius2InstanceConfig} based on some selection strategy.
 * <p>
 * If no config based override is applied, this Factory will automatically detect whether the
 * current deployment environment is EC2 or not, and create the appropriate Config instances.
 * <p>
 * Setting the property <b>eureka.instanceDeploymentEnvironment=ec2</b> will force the instantiation
 * of {@link com.netflix.appinfo.Ec2EurekaArchaius2InstanceConfig}, regardless of what the
 * automatic environment detection says.
 * <p>
 * Setting the property <b>eureka.instanceDeploymentEnvironment={a non-null, non-ec2 string}</b>
 * will force the instantiation of {@link com.netflix.appinfo.EurekaArchaius2InstanceConfig},
 * regardless of what the automatic environment detection says.
 * <p>
 * Why define the {@link com.netflix.appinfo.providers.EurekaInstanceConfigFactory} instead
 * of using {@link javax.inject.Provider} instead? Provider does not work due to the fact that
 * Guice treats Providers specially.
 *
 * @author David Liu
 */
@Singleton
@ConfigurationSource(CommonConstants.CONFIG_FILE_NAME)
public class CompositeInstanceConfigFactory implements EurekaInstanceConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(CompositeInstanceConfigFactory.class);

    private static final String DEPLOYMENT_ENVIRONMENT_OVERRIDE_KEY = "instanceDeploymentEnvironment";

    private final String namespace;
    private final Config configInstance;
    private final InternalPrefixedConfig prefixedConfig;

    private EurekaInstanceConfig eurekaInstanceConfig;

    @Inject
    public CompositeInstanceConfigFactory(Config configInstance, String namespace) {
        this.configInstance = configInstance;
        this.namespace = namespace;
        this.prefixedConfig = new InternalPrefixedConfig(configInstance, namespace);
    }

    @Override
    public synchronized EurekaInstanceConfig get() {
        if (eurekaInstanceConfig == null) {
            // create the amazonInfoConfig before we can determine if we are in EC2, as we want to use the amazonInfoConfig for
            // that determination. This is just the config however so is cheap to do and does not have side effects.
            eurekaInstanceConfig = new EurekaArchaius2InstanceConfig(configInstance, namespace);

            // TODO: Remove this when DiscoveryManager is finally no longer used
            DiscoveryManager.getInstance().setEurekaInstanceConfig(eurekaInstanceConfig);
        }

        return eurekaInstanceConfig;
    }

    private String getDeploymentEnvironmentOverride() {
        return prefixedConfig.getString(DEPLOYMENT_ENVIRONMENT_OVERRIDE_KEY, null);
    }
}
