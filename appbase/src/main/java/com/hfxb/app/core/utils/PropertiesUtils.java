package com.hfxb.app.core.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.nio.charset.Charset;

public class PropertiesUtils {
	
	/**
	 * 
	 * <p>加载属性文件</p>
	 * @param @param fileName
	 * @param @param charset
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return PropertiesConfiguration
	 * @throws
	 */
	public static PropertiesConfiguration load(String fileName,String charset) throws ConfigurationException {
		return load(fileName, charset, false);
	}
	
	/**
	 * 
	 * <p>加载属性文件</p>
	 * @param @param fileName
	 * @param @param charset
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return PropertiesConfiguration
	 * @throws
	 */
	public static PropertiesConfiguration load(String fileName,String charset, boolean reload) throws ConfigurationException {
		PropertiesConfiguration conf = new PropertiesConfiguration();
		conf.setEncoding(charset);
		conf.setFileName(fileName);
		if(reload) {
			conf.setReloadingStrategy(new FileChangedReloadingStrategy());
		}
		conf.load();
		return conf;
	}
	
	/**
	 * 
	 * <p>加载属性文件</p>
	 * @param @param fileName
	 * @param @param charset
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return PropertiesConfiguration
	 * @throws
	 */
	public static PropertiesConfiguration load(String fileName,Charset charset) throws ConfigurationException {
		return load(fileName, charset.name());
	}
	
	/**
	 * 
	 * <p>加载属性文件</p>
	 * @param @param fileName
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return PropertiesConfiguration
	 * @throws
	 */
	public static PropertiesConfiguration load(String fileName) throws ConfigurationException {
		return new PropertiesConfiguration(fileName);
	}

	/**
	 * 
	 * <p>加载ini配置文件</p>
	 * @param @param fileName
	 * @param @param charset
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return HierarchicalINIConfiguration
	 * @throws
	 */
	public static HierarchicalINIConfiguration loadIni(String fileName, String charset) throws ConfigurationException {
		HierarchicalINIConfiguration conf = new HierarchicalINIConfiguration();
		conf.setEncoding(charset);
		conf.setFileName(fileName);
		conf.load();
		return conf;
	}
	
	/**
	 * 
	 * <p>加载ini配置文件</p>
	 * @param @param fileName
	 * @param @param charset
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return HierarchicalINIConfiguration
	 * @throws
	 */
	public static HierarchicalINIConfiguration loadIni(String fileName, Charset charset) throws ConfigurationException {
		return loadIni(fileName, charset.displayName());
	}
	
	/**
	 * 
	 * <p>加载ini配置文件</p>
	 * @param @param fileName
	 * @param @return
	 * @param @throws ConfigurationException
	 * @return HierarchicalINIConfiguration
	 * @throws
	 */
	public static HierarchicalINIConfiguration loadIni(String fileName) throws ConfigurationException {
		return new HierarchicalINIConfiguration(fileName);
	}
}
