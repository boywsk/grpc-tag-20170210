package com.gomeplus.grpc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产RPC java文件的工具类
 * @author liuzhenhuan
 *
 */
public class GenerateRPCJavaUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(GenerateRPCJavaUtil.class);
	public static void main(String[] args) {
		String protocExePath="E:/workspaces/MyEclipse2016/TestProto/src/protoc-3.0.0-windows-x86_64.exe";
		String grpcExePath="E:/repository/io/grpc/protoc-gen-grpc-java/1.0.1/protoc-gen-grpc-java-1.0.1-windows-x86_64.exe";
		String protoDectoryPath="src/main/proto";
//		String customerPath="E:/workspaces/MyEclipse2016/OAServer/gomeplus-im-api/src/main/java";
		String[] javaOutPathList=new String[]{"target/generated-sources/protobuf/java"};
		String[] grpcOutPathList=new String[]{"target/generated-sources/protobuf/grpc-java"};
//		String[] protoFilePathList=new String[]{"GroupServices.proto","JedisClusterServices.proto"};
		//String[] allFilePathInDirectory = getAllFilePathInDirectory(protoDectoryPath);
		generateProtobufJava(protocExePath, protoDectoryPath, null, javaOutPathList);
		generateGRPCJava(protocExePath, grpcExePath, protoDectoryPath, null, grpcOutPathList);
	}
	
	/**
	 * 生产protobuf-java 源文件
	 * @param protocExePath protoc.exe执行路径
	 * @param protoDectoryPath .proto文件目录
	 * @param protoFilePaths .proto文件名 支持多个文件 
	 * @param javaOutPaths java输出目录 支出多个java文件输出路径
	 */
	public static boolean generateProtobufJava(String protocExePath,String protoDectoryPath,String[] protoFilePaths,String[] javaOutPaths){
		try {
			String protoFilePath="";
			String javaOutPath="";
			for(String path:javaOutPaths){
				path="\""+path+"\"";
				protoFilePath+=(" --java_out="+path);
			}
			//确保.proto文件目录正确
			if (!protoDectoryPath.endsWith("/")) {
				protoDectoryPath=protoDectoryPath+"/";
			}
			if (ArrayUtils.isEmpty(protoFilePaths)) {//若.proto 文件列表问空，则默认取proto目录下面的所有文件
				protoFilePaths=getAllFilePathInDirectory(protoDectoryPath,".proto");
			}
			for (String path : protoFilePaths) {
				path="\""+path+"\"";
				javaOutPath+=" "+protoDectoryPath+path;
			}
			//保证路径有效 否则路径中包含空格会报异常
			protocExePath="\""+protocExePath+"\"";
			String strCmd = protocExePath+" --proto_path="+protoDectoryPath+protoFilePath +javaOutPath; 
			boolean isSuccess = exec(strCmd);
			if (isSuccess) {
				logger.info("generateProtobufJava success");
			}else {
				logger.info("generateProtobufJava error");
			}
			return true;
		} catch (Exception e) {
			logger.error("generateProtobufJava error",e);
			return false;
		}
	}
	/**
	 * 生产GRPC-java源文件
	 * @param protocExePath protoc.exe执行路径
	 * @param protoDectoryPath .proto文件目录
	 * @param protoFilePaths .proto文件名 支持多个文件 
	 * @param javaOutPaths java输出目录 支出多个java文件输出路径
	 */
	public static boolean generateGRPCJava(String protocExePath,String grpcExePath,String protoDectoryPath,String[] protoFilePaths,String[] javaOutPaths){
		try {
			String protoFilePath="";
			String javaOutPath="";
			for(String path:javaOutPaths){
				path="\""+path+"\"";
				protoFilePath+=(" --grpc-java_out="+path);
			}
			//确保.proto文件目录正确
			if (!protoDectoryPath.endsWith("/")) {
				protoDectoryPath=protoDectoryPath+"/";
			}
			if (ArrayUtils.isEmpty(protoFilePaths)) {//若.proto 文件列表问空，则默认取proto目录下面的所有文件
				protoFilePaths=getAllFilePathInDirectory(protoDectoryPath,".proto");
			}
			for (String path : protoFilePaths) {
				path="\""+path+"\"";
				javaOutPath+=" "+protoDectoryPath+path;
			}
			//保证路径有效 否则路径中包含空格会报异常
			protocExePath="\""+protocExePath+"\"";
			grpcExePath=" --plugin=protoc-gen-grpc-java="+"\""+grpcExePath+"\"";
			
			String strCmd = protocExePath+grpcExePath+" --proto_path="+protoDectoryPath+protoFilePath +javaOutPath; 
			boolean isSuccess = exec(strCmd);
			if (isSuccess) {
				logger.info("generateGRPCJava success");
			}else {
				logger.info("generateGRPCJava error");
			}
			return true;
		} catch (Exception e) {
			logger.error("generateGRPCJava error",e);
			return false;
		}
	}
	
	/**
	 *执行命令
	 * @param cmd
	 * @return
	 */
	private static boolean exec(String cmd){
		try {
        	Runtime runtime = Runtime.getRuntime();
        	Process process = runtime.exec(cmd);
        	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        	BufferedReader errotBr = new BufferedReader(new InputStreamReader(process.getErrorStream()));  
        	String errorline; 
        	while((errorline=errotBr.readLine()) != null){  
        		logger.error("process error,info:{}",errorline);
        		return false;
        	}  
        	String line; 
        	while((line=br.readLine()) != null){  
        		logger.info("process success,info:{}",line);
        		return true;
        	}  
        	logger.info("process success");
        	return true;
        } catch (Exception e) {
        	logger.error("exec error ",e);
        	return false;
        }
	}
	
	/**
	 * 得到某个目录下的所有文件名称
	 * @param directoryPath
	 * @param fileSuffix 文件后缀
	 * @return
	 */
	private static String[] getAllFilePathInDirectory(String directoryPath,final String fileSuffix){
		try {
			File file = new File(directoryPath);
			if (!file.exists()) {
				logger.error("getAllFilePathInDirectory file:{} is not exists",file.getAbsoluteFile());
				return null;
			}
			if (!file.isDirectory()) {
				logger.error("getAllFilePathInDirectory file:{} is not a directory",file.getAbsoluteFile());
				return null;
			}
			FilenameFilter filter=new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (StringUtils.endsWith(name, fileSuffix)) {
						return true;
					}
					return false;
				}
			};
			String[] fileNames = file.list(filter);
			if (ArrayUtils.isEmpty(fileNames)) {
				logger.error("getAllFilePathInDirectory file:{} fileNamelist is empty",file.getAbsoluteFile());
				return null;
			}
			return fileNames;
		} catch (Exception e) {
			logger.error("getAllFilePathInDirectory error ",e);
			return null;
		}
	}
	
}
