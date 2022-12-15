package com.azure.webapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.AzureAuthorityHosts;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appservice.models.PricingTier;
import com.azure.resourcemanager.appservice.models.WebApp;


@Component
public class WebAppAzure {

	@Value("${azuredemo.clientId}")
	private String clientId;
	
	@Value("${azuredemo.clientSecret}")
	private String clientSecret;
	
	@Value("${azuredemo.tenantId}")
	private String tenantId;
	
	public boolean createWebApp() {
        try {

            final String appName = "BatchDemo";

            System.out.println(clientId + " - " +clientSecret);
            
            TokenCredential credential = new ClientSecretCredentialBuilder().clientId("62fff4e9-37a9-4f80-a3eb-441d2d68ae1e")
            		.clientSecret("L5E8Q~pD46Qgthh6NSl~2K70R0yl6oVB0luXPaJJ").tenantId("cc2f2118-790f-4e3b-b7ed-31e9c37987c4")
	                .authorityHost(AzureAuthorityHosts.AZURE_PUBLIC_CLOUD)
	                .build();
            
            // If you don't set the tenant ID and subscription ID via environment variables,
            // change to create the Azure profile with tenantId, subscriptionId, and Azure environment.
            AzureProfile profile = new AzureProfile("cc2f2118-790f-4e3b-b7ed-31e9c37987c4",
					"4dac1af5-92b8-490e-880b-4cf4c4856b14",AzureEnvironment.AZURE);
            
            AzureResourceManager azureResourceManager = AzureResourceManager.configure()
                    .withLogLevel(HttpLogDetailLevel.BASIC)
                    .authenticate(credential, profile)
                    .withDefaultSubscription();

            WebApp app = azureResourceManager.webApps().define(appName)
                    .withRegion(Region.US_EAST)
                    .withNewResourceGroup("sampleWebResourceGroup")
                    .withNewWindowsPlan(PricingTier.FREE_F1)
                    .defineSourceControl()
                    .withPublicGitRepository("https://github.com/manimaran6483/spring-batch-employee")
                    .withBranch("main")
                    .attach()
                    .create();

            return true;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

	
}
