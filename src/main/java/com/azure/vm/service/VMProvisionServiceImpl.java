package com.azure.vm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.AzureAuthorityHosts;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.compute.models.DiskInstanceView;
import com.azure.resourcemanager.compute.models.InstanceViewStatus;
import com.azure.resourcemanager.compute.models.KnownLinuxVirtualMachineImage;
import com.azure.resourcemanager.compute.models.VirtualMachine;
import com.azure.resourcemanager.compute.models.VirtualMachineSizeTypes;

@Component
public class VMProvisionServiceImpl implements VMProvisionService{

	
	@Value("${azuredemo.clientId}")
	private String clientId;
	
	@Value("${azuredemo.clientSecret}")
	private String clientSecret;
	
	@Value("${azuredemo.tenantId}")
	private String tenantId;
	
	@Override
	public boolean provisionVM() {
		
		final String userName = "MyAzureVMJava";
        final String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCvFWNprUM/AY8HTI1TwFK+RcPnvZZC7pJb82ohRchjo2cBhnhk8YoYdhzgO7/attisFHbObKM3uYHncvs5Xy/dhIal/RUIrXTtVuMU3PXC78UvNn9Im1bd688YAJzVQBjIZ3wkZP4sCR31XrNyF9BfvmCl0ewk5JxSNjkCfEw4wQI63AB0IvtWNaXS4WJdo+lEoctWVWPGD9vNxEU9GCoTEKKuq1vA9XNc8QW9FJ+0ep499+mNOqBZdvmIiFAMy2N4N2fnaqFGZQqUiWVajrq5+IO/3UqkmYSQy/sW1omtjf8SVlmhuLsAKIpmiV3P6ZLV72WkjtBcjpgVthnw2wKkAnDHYDbqIhj2H2txUe4ROr39mmBajbCBJ6/BNqZkK1SYY6/uGJnf+Zvm8/h4gyrls3/E9qKOGQLB/GMc3qtVlBJvnto3LdDenJnqCiODqoJAG70EcnYFTPdVdoZczWtFEF+hJUkyZjbRaCG6jXuwJiuDEiJZmut9QgUzEDSEKP0= generated-by-azure";
		
		try {
			
			/*
			 * TokenCredential credential = new DefaultAzureCredentialBuilder()
			 * .authorityHost(AzureAuthorityHosts.AZURE_PUBLIC_CLOUD) .build();
			 */
			
			TokenCredential credential = new ClientSecretCredentialBuilder().clientId(clientId).clientSecret(clientSecret).tenantId(tenantId)
	                .authorityHost(AzureAuthorityHosts.AZURE_PUBLIC_CLOUD)
	                .build();
			
			//AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
			
			AzureProfile profile = new AzureProfile("cc2f2118-790f-4e3b-b7ed-31e9c37987c4",
					"4dac1af5-92b8-490e-880b-4cf4c4856b14",AzureEnvironment.AZURE);

	        AzureResourceManager azureResourceManager = AzureResourceManager.configure()
	                .withLogLevel(HttpLogDetailLevel.BASIC)
	                .authenticate(credential, profile)
	                .withDefaultSubscription();
			
	     // Create an Ubuntu virtual machine in a new resource group.
	        VirtualMachine linuxVM = azureResourceManager.virtualMachines().define("testLinuxVM")
	                .withRegion(Region.US_EAST)
	                .withNewResourceGroup("sampleVmResourceGroup")
	                .withNewPrimaryNetwork("10.0.0.0/24")
	                .withPrimaryPrivateIPAddressDynamic()
	                .withoutPrimaryPublicIPAddress()
	                .withPopularLinuxImage(KnownLinuxVirtualMachineImage.UBUNTU_SERVER_18_04_LTS)
	                .withRootUsername(userName).withRootPassword("aLk$u.fTEbzeb#gd")
	                .withSsh(sshKey)
	                .withSize(VirtualMachineSizeTypes.STANDARD_B1S)
	                .create();
	        
	        
	        System.out.println("hardwareProfile");
			System.out.println("    vmSize: " + linuxVM.size());
			System.out.println("storageProfile");
			System.out.println("  imageReference");
			System.out.println("    publisher: " + linuxVM.storageProfile().imageReference().publisher());
			System.out.println("    offer: " + linuxVM.storageProfile().imageReference().offer());
			System.out.println("    sku: " + linuxVM.storageProfile().imageReference().sku());
			System.out.println("    version: " + linuxVM.storageProfile().imageReference().version());
			System.out.println("  osDisk");
			System.out.println("    osType: " + linuxVM.storageProfile().osDisk().osType());
			System.out.println("    name: " + linuxVM.storageProfile().osDisk().name());
			System.out.println("    createOption: " + linuxVM.storageProfile().osDisk().createOption());
			System.out.println("    caching: " + linuxVM.storageProfile().osDisk().caching());
			System.out.println("osProfile");
			System.out.println("    computerName: " + linuxVM.osProfile().computerName());
			System.out.println("    adminUserName: " + linuxVM.osProfile().adminUsername());
			System.out.println("    provisionVMAgent: " + linuxVM.osProfile().windowsConfiguration().provisionVMAgent());
			System.out.println(
			        "    enableAutomaticUpdates: " + linuxVM.osProfile().windowsConfiguration().enableAutomaticUpdates());
			System.out.println("networkProfile");
			System.out.println("    networkInterface: " + linuxVM.primaryNetworkInterfaceId());
			System.out.println("vmAgent");
			System.out.println("  vmAgentVersion: " + linuxVM.instanceView().vmAgent().vmAgentVersion());
			System.out.println("    statuses");
			for (InstanceViewStatus status : linuxVM.instanceView().vmAgent().statuses()) {
			    System.out.println("    code: " + status.code());
			    System.out.println("    displayStatus: " + status.displayStatus());
			    System.out.println("    message: " + status.message());
			    System.out.println("    time: " + status.time());
			}
			System.out.println("disks");
			for (DiskInstanceView disk : linuxVM.instanceView().disks()) {
			    System.out.println("  name: " + disk.name());
			    System.out.println("  statuses");
			    for (InstanceViewStatus status : disk.statuses()) {
			        System.out.println("    code: " + status.code());
			        System.out.println("    displayStatus: " + status.displayStatus());
			        System.out.println("    time: " + status.time());
			    }
			}
			System.out.println("VM general status");
			System.out.println("  provisioningStatus: " + linuxVM.provisioningState());
			System.out.println("  id: " + linuxVM.id());
			System.out.println("  name: " + linuxVM.name());
			System.out.println("  type: " + linuxVM.type());
			System.out.println("VM instance status");
			for (InstanceViewStatus status : linuxVM.instanceView().statuses()) {
			    System.out.println("  code: " + status.code());
			    System.out.println("  displayStatus: " + status.displayStatus());
			}
			
			return true;
		}catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
		
		
        
		return false;
	}

}
