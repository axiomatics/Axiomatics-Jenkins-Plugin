package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

public class AxiomaticsBuilder extends Builder implements SimpleBuildStep {

	private final String name;
	private boolean useFrench;
	private final String asmURL;
	private final String wsdlURL;
	private final String asmUser;
	private final String asmPassword;
	private final String trustStore;
	private final String trustStoreType;
	private final String trustStorePassword;
	private final String domainName;
	private final String projectName;

	public String getProjectName() {
		return projectName;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public String getTrustStoreType() {
		return trustStoreType;
	}

	public String getTrustStore() {
		return trustStore;
	}

	public String getAsmPassword() {
		return asmPassword;
	}

	public String getAsmUser() {
		return asmUser;
	}

	public String getWsdlURL() {
		return wsdlURL;
	}

	public String getAsmURL() {
		return asmURL;
	}

	@DataBoundConstructor
	public AxiomaticsBuilder(String name, String asmURL, String wsdlURL, String asmUser, String asmPassword,
			String trustStore, String trustStoreType, String trustStorePassword, String domainName, String projectName) {
		this.name = name;
		this.asmURL = asmURL;
		this.wsdlURL = wsdlURL;
		this.asmUser = asmUser;
		this.asmPassword = asmPassword;
		this.trustStore = trustStore;
		this.trustStoreType = trustStoreType;
		this.trustStorePassword = trustStorePassword;
		this.domainName = domainName;
		this.projectName = projectName;
	}

	public String getName() {
		return name;
	}

	public boolean isUseFrench() {
		return useFrench;
	}

	@DataBoundSetter
	public void setUseFrench(boolean useFrench) {
		this.useFrench = useFrench;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {

		run.addAction(new AxiomaticsAction(name, asmURL, wsdlURL, asmUser, asmPassword, trustStore, trustStoreType,
				trustStorePassword, domainName, projectName));
		if (useFrench) {
			listener.getLogger().println("Bonjour, " + name + "!");
		} else {
			listener.getLogger().println("Hello, " + name + "!");
			listener.getLogger().println("Your ASM URL: " + asmURL + "!");
			listener.getLogger().println("Your WSDL URL: " + wsdlURL + "!");
		}
	}

	@Symbol("greet")
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error(Messages.AxiomaticsBuilder_DescriptorImpl_errors_missingName());
			if (value.length() < 4)
				return FormValidation.warning(Messages.AxiomaticsBuilder_DescriptorImpl_warnings_tooShort());
			if (!useFrench && value.matches(".*[éáàç].*")) {
				return FormValidation.warning(Messages.AxiomaticsBuilder_DescriptorImpl_warnings_reallyFrench());
			}
			return FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return Messages.AxiomaticsBuilder_DescriptorImpl_DisplayName();
		}

	}

}
