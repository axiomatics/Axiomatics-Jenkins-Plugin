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
	

	// Your ASM login
	//private String user = "asm-admin";
	
	// Your ASM password
	private String password = "password";
	
	// Your truststore path
	private String trustStore = "/Users/mikegood/Documents/jenkinscotent/keystore.jks";
	
	// Your truststore type
	private String trustStoreType = "jks";
	
	// Your truststore password
	private String trustStorePassword = "changeit";
	
	// Your WSDL URL
	//private String wsdlUrl = "https://ec2-34-207-78-79.compute-1.amazonaws.com:8443/asm/admin?wsdl";
	
	// Your domain name
	private String domainName = "TutorialDomain";
	
	// Your project name
	private String projectName = "TutorialProject";

	// Default constructor
	public UploadPolicy() {

	}
	

	// Method for uploading...
	public String setParameters(String policyFile, String asmURL, String wsdlURL, String asmUser)
			throws HttpException, AsmAccessDenied_Exception, AsmWebServiceFault_Exception, IOException {
		
		
		//System.out.println("Here is the content of axiomaticsBuilder.getAsmURL() =" + axiomaticsBuilder.getAsmURL() );
		// Set all the ASM runtime parameters
		ClientInfo clientInfo = new ClientInfo(asmURL);
		clientInfo.setUser(asmUser);
		clientInfo.setWsdlUrl(wsdlURL);
		clientInfo.setPassword(password);
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
