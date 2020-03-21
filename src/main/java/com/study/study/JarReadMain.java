package com.study.study;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;


public class JarReadMain {
	
	public static String META_INF = "META-INF";
	
	public static String SOFA_VERSION = "sofa.version";
	
	public static String APP_VERSION = "app.version";
	
	public static String WEB_INF = "WEB-INF";
	
	public static String SOFA_CONTAINER = "sofa-container";
	
	public static String KERNEL = "kernel";
	
	public static String REPOSITORY = "repository";
	
	public static String JAR_SUFFIX = ".jar";
	
	public static void main(String[] args) {
		String acsLine = "";
		File rootFile = null;
		System.out.println("请输入WEB-INF文件夹所在的全路径");
		Scanner sc = new Scanner(System.in);
		while(rootFile == null || !rootFile.exists()) {
			acsLine = sc.nextLine();
			rootFile = new File(acsLine);
			if (!rootFile.exists()) {
				System.out.println("找不到输入路径下的文件，请重新输入");
			}
		}
		
		System.out.println("请输入需要搜索的jar信息");
		String keyword = sc.nextLine();
		keyword = StringUtils.isEmpty(keyword) ? "" : keyword.trim();
		while(!"N".equals(keyword)) {
			File[] listFiles = rootFile.listFiles();
			StringBuffer sofaSb = new StringBuffer();
			StringBuffer jarSb = new StringBuffer();
			for (File subFile : listFiles) {
				
				//读取sofa版本信息
				if (subFile.getName() != null && subFile.getName().contains(META_INF)) {
					sofaSb.append(readSofaVersion(subFile));
				}
				
				//读取jar包的信息
				if (subFile.getName() != null && subFile.getName().contains(WEB_INF)) {
					File[] listF = subFile.listFiles();
					for (File file : listF) {
						if (file != null && file.getName() != null && file.getName().equals(SOFA_CONTAINER)) {
							//目前只读取kernel 和repository文件夹下的内容
							File[] listFiles2 = file.listFiles();
							for (File file2 : listFiles2) {
								if (file2.exists() && 
										(file2.getName().equals(KERNEL) || file2.getName().equals(REPOSITORY)) && file2.isDirectory()) {
									jarSb.append(readFileVersion(file2, keyword));
								}
							}
						}
					}
				}
			}
			
			sofaSb.append(System.lineSeparator()).append(jarSb);
			System.out.println(sofaSb.toString());
			System.out.println();
			System.out.println("如需继续，请输入对应的jar信息，不想继续 请输入 N");
			keyword = sc.nextLine();
			keyword = StringUtils.isEmpty(keyword) ? "" : keyword.trim();
		}
		sc.close();

	}
	
	/**
	 * 读取sofa 的版本信息
	 * @param file 读取file文件
	 * @return sofa的版本信息
	 */
	public static StringBuffer readSofaVersion(File file) {
		StringBuffer sb = new StringBuffer();
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.exists() && file2.getName().equals(SOFA_VERSION)) {
				try {
					
					sb.append("技术平台版本：");
					BufferedReader read = new BufferedReader(new FileReader(file2));
					String s = null;
					while ((s = read.readLine()) != null) {
						sb.append(" " + s);
					}
					sb.append(System.lineSeparator());
					if (read != null) {
						read.close();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (file2.exists() && file2.getName().equals(APP_VERSION)) {
				try {
					
					sb.append("应用系统版本：");
					BufferedReader read = new BufferedReader(new FileReader(file2));
					String s = null;
					while ((s = read.readLine()) != null) {
						sb.append(" " + s);
					}
					sb.append(System.lineSeparator());
					if (read != null) {
						read.close();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
	}
	
	/**
	 * 读文件，获取里面的版本和构建信息
	 * @param file 传入的文件信息
	 * @param keyword 输入的关键字
	 * @return 版本和构建信息
	 */
	public static StringBuffer readFileVersion (File file, String keyword) {
		StringBuffer sb = new StringBuffer();
		if (file == null || !file.exists()) {
			return sb;
		}
		
		//如果是单个的jar文件，直接读取
		if (file.getName() != null && file.getName().contains(JAR_SUFFIX) && file.getName().contains(keyword)) {
			sb.append(readJar(file, keyword));
		} else if (file.isDirectory() && (!(file.getName().contains("lib") || file.getName().contains("3rd") || file.getName().contains("acs")))){
			//如果是文件夹，循环遍历，查找jar文件
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				sb.append(readFileVersion(file2, keyword));
			}
		}
		return sb;
	}
	
	/**
	 * 传入需要读取的jar文件，将 Built-At、 Bundle-Version 的信息返回。
	 * @param file 传入jar文件信息
	 * @param keyword 输入的关键字
	 * @return 版本信息
	 */
	public static StringBuffer readJar(File file, String keyword) {
		
		StringBuffer sb = new StringBuffer();
		if (file.exists() && file.getName().contains(JAR_SUFFIX) && file.getName().contains(keyword)) {
			try {
				sb.append(file.getName() + "  ");
				JarFile jarFile = new JarFile(file);
				Manifest manifest = jarFile.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				String value = attributes.getValue("Built-At");
				sb.append("Built-At: ").append(StringUtils.isEmpty(value) ? "-" : value);
				String value1 = attributes.getValue("Bundle-Version");
				sb.append("  Bundle-Version: ").append(StringUtils.isEmpty(value1) ? "-" : value1);
				if (jarFile != null) {
					jarFile.close();
				}
				sb.append(System.lineSeparator());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

}
