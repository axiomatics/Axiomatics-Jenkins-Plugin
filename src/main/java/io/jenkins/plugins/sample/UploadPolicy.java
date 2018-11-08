package io.jenkins.plugins.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.httpclient.HttpException;

import com.axiomatics.asm.admin.client.AsmAccessDenied_Exception;
import com.axiomatics.asm.admin.client.AsmAdminService;
import com.axiomatics.asm.admin.client.AsmWebServiceFault_Exception;
import com.axiomatics.asm.admin.client.TransferablePolicyPackage;
import com.axiomatics.asm.admin.client.XmlDomain;
import com.axiomatics.asm.client.AsmAdminClient;
import com.axiomatics.asm.client.ClientInfo;


public class UploadPolicy {
	
	// Your domain name
	//private String domainName = "TutorialDomain";
	
	// Your project name
	//private String projectName = "TutorialProject";

	// Default constructor
	public UploadPolicy() {

	}
	

	// Method for uploading...
	public String setParameters(String policyFile, String asmURL, String wsdlURL, String asmUser, String asmPassword,
			String trustStore, String trustStoreType, String trustStorePassword, String domainName, String projectName)
			throws HttpException, AsmAccessDenied_Exception, AsmWebServiceFault_Exception, IOException {
		
		
		//System.out.println("Here is the content of axiomaticsBuilder.getAsmURL() =" + axiomaticsBuilder.getAsmURL() );
		// Set all the ASM runtime parameters
		ClientInfo clientInfo = new ClientInfo(asmURL);
		clientInfo.setUser(asmUser);
		clientInfo.setWsdlUrl(wsdlURL);
		clientInfo.setPassword(asmPassword);
		clientInfo.setTrustStoreType(trustStoreType);
		clientInfo.setTrustStore(trustStore);
		clientInfo.setTrustStorePassword(trustStorePassword);

		AsmAdminClient asmAdminClient = new AsmAdminClient(clientInfo);
		AsmAdminService srv = asmAdminClient.getConnection();
		
		// Test the connection
		@SuppressWarnings("unused")
		String testConnection = srv.testConnection();

		System.out.println("\n--- UPLOADING POLICY TO DOMAIN---");
		
		File policyZip = new File(policyFile);
		byte[] policyStr = new byte[(int) policyZip.length()];
		InputStream is = new FileInputStream(policyZip);
		int numByte = is.read(policyStr);
		is.close();		
		
		TransferablePolicyPackage ppkg = new TransferablePolicyPackage();
		ppkg.setPolicyBytes(policyStr);
		System.out.println("Number of bytes read: " + numByte);

		List<XmlDomain> domains = srv.getDomainByName(domainName, projectName);		
		for(XmlDomain domain : domains){
			System.out.println("Found Domain with name: " + domainName);
			
			srv.assignPolicyToDomainViaPackage(ppkg, domain.getId(), projectName);
			System.out
			        .println("----------------------DONE UPLOADING POLICY TO DOMAIN-----------------------------\n");

		}
		return testConnection;
	}

}
