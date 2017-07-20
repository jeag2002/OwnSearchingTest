package es.gromenauer.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Utility class. Tool for operating with folder and files
 * @author Usuario
 *
 */

public final class FileUtils {
	
	//create new folder
	public static void createFolder(String parentPath, String newFolder) throws IOException, SecurityException{
		
		String path = parentPath + File.separatorChar + newFolder;
		File fil = new File(path);
		if (!fil.exists()){
			fil.mkdir();
		}
		
	}
	
	//delete folder and all content.
	public static void deleteFolder(String parentPath, String newFolder) throws IOException, SecurityException{
		
		String path = parentPath + File.separatorChar + newFolder;
		
		File folder = new File(path);
		boolean statDel = false;
		
		if(folder.exists()){
			File listFiles[] = folder.listFiles();
			if (listFiles != null){
				if (listFiles.length > 0){
					for(File fil: listFiles){
						statDel = fil.delete();
						if (!statDel){
							throw new IOException("error when try to delete file (" + fil.getAbsolutePath() + ")");
						}
					}
				}
			}
			
			statDel = folder.delete();
			if (!statDel){
				throw new IOException("error when try to delete folder (" + folder.getAbsolutePath() + ")");
			}
		}
		
	}
	
	//get list of files of a directory
	public static File[] getListOfFile(File dir) throws IOException{
		
		File[] listOfFiles = dir.listFiles(
								new FileFilter() {
									public boolean accept(File pathname) {
											return (!pathname.isDirectory());
									}
								}
							);
		return listOfFiles;	
	}


}
