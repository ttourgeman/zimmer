package org.home.zimmer.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;


public class ZimmerConfig {
	
	private static ZimmerConfig s_instance = new ZimmerConfig();
	private static Map<String,String> fileContentTypes = new HashMap<String,String>();
	
	private XMLConfiguration config;


	public final static String globalConfigFileName = "SimbaMainSettings.xml";

	private ZimmerConfig(){
		if (config == null) {
			try {
				// read the config file
				URL configURL = ZimmerConfig.class.getClassLoader().getResource(globalConfigFileName);
				config = new XMLConfiguration(configURL);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
			// here we load the contentTypes from the resource file		
			try{
				URL inputUrl =  ClassLoader.class.getResource("/ContentFileTypes.tsv");
				File contentFile = new File(inputUrl.getFile());
				FileReader reader = new FileReader(contentFile);
				BufferedReader br = new BufferedReader(reader);
				String line;
		        while ((line = br.readLine()) != null) {
		            String[] data = StringUtils.split(line, "\t");
		            if(data.length>1){
		            	fileContentTypes.put(data[0], data[1]);
		            }
		        }
		        reader.close();
		        br.close();
			}
			catch(Exception e){
			//	logger.error("Failed to load file content types.");
			}
		}	
	}
	
	public static void reloadConfiguration() throws ConfigurationException{
		s_instance.config.reload();
	}
	
	// The local root folder to save all the simba data
	public static String getSimbaRootLocalFolder() {
		return s_instance.config.getString("data.localRootFolder", "/usr/simba");
	}
	// Feed file form submit
	public static String getFeedIdParamSubmitFormName() {
		return s_instance.config.getString("feeds.file.sumbmitForm.feedIdParamName", "feedId");
	}
	public static String getFeedLocalFolderToSaveFiles() {
		String folderPath = s_instance.config.getString("feeds.file.localFolder", "/feeds/uploads");
		return getSimbaRootLocalFolder() + folderPath;
	}

	public static String getWriterLocalDownloadFolder() {
		String folderPath = s_instance.config.getString("writers.localJobFolder", "/jobs/downloads");
		return getSimbaRootLocalFolder() + folderPath;
	}
	public static int getDaysToKeepDownloadedFiles() {
		return s_instance.config.getInt("writers.daysToKeepFiles",7);
	}
	public static String getFeedNoteParamSubmitFormName() {
		return s_instance.config.getString("feeds.file.sumbmitForm.fileNoteParamName", "feedNote");
	}

	public static String getMainEmailSenderName() {
		return s_instance.config.getString("info.mail.mainSender", "SimbaService-DO-NOT-REPLY@ebay.com");
	}

	public static int getJobsExcutionPoolLimit() {
		return s_instance.config.getInt("jobs.excution.poolSize",3);
	}

	public static int getSessionTimeout() {
		return s_instance.config.getInt("session.timeout",10000000);
	}


	public static String getSimbaMPS2ClientName() {
		return s_instance.config.getString("info.mps2.client.name", "SimbaServiceMPS");
	}
	
	public static String getFileContentType(String fileType){
		return fileContentTypes.get(fileType);
	}	
	
	public static String getSimbaVersion()
	{
		return s_instance.config.getString("version", "Unknown");
	}
	
	public static String getSimbaMachineSessionId()
	{
		return s_instance.config.getString("machines.key", "536b7f04e4b0857d2cc7abcc");
	}


}
