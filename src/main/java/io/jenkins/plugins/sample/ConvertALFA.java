package io.jenkins.plugins.sample;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class ConvertALFA {

	private File mainPolicy;
	private File policyFolder;
	
	public ConvertALFA() {
		
	}
	public ConvertALFA(File mainPolicy, File policyFolder) {
	        this.mainPolicy = mainPolicy;
	        this.policyFolder = policyFolder;
	    }

	public File producePackage(String destination) throws IOException {
		ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destination));

		File[] files = policyFolder.listFiles();

		for (File f : files) {
			// If the file is not a folder and it is not the main policy
			if (f.isFile() && f.getAbsolutePath().equals(mainPolicy.getAbsolutePath()) == false) {
				addZipEntry("referenceable/" + f.getName(), f, zip);
			}
		}
		// Add the main policy now
		addZipEntry("root-policy.xml", mainPolicy, zip);

		zip.close();

		return new File(destination);
	}

	private void addZipEntry(String entryName, File f, ZipOutputStream zip) throws IOException {
		ZipEntry zentry = new ZipEntry(entryName);
		zip.putNextEntry(zentry);
		FileInputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		int count;
		while ((count = in.read(b)) > 0) {
			System.out.println();
			zip.write(b, 0, count);
		}
		in.close();
	}

	public File doThePackaging(File mainPolicy, File policyFolder, String destPackage ) {
	
		/*
		 * mainPolicy
		 * 
		 * This is the policy file that is the root "entry point" to the policy package
		 *
		 */

		/*
		 * policyFolder
		 * This is the directory that contains all of the other policy files
		 */
	
		/*
		 * destPackage
		 * This is the name and path of the policy package to be created destPackage =
		 */
		
		File policyPackage = null;
		 if (mainPolicy.exists()==false){
	            System.err.println(mainPolicy.getAbsolutePath()+" does not exist.");
	            System.exit(0);
	        }   
		
		if (policyFolder.exists() == false || policyFolder.isDirectory() == false) {
			System.err.println(policyFolder.getAbsolutePath() + " does not exist or is not a folder.");
			System.exit(0);
		}
		ConvertALFA p = new ConvertALFA(mainPolicy, policyFolder);
		try {
			policyPackage = p.producePackage(destPackage);
			System.out.println("Package written to " + policyPackage.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return policyPackage;
	}

}
