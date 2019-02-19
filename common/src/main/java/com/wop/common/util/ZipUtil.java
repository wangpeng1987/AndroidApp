package com.wop.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;

/**
 * Created by liuwenji on 2018/10/11.
 * 处理压缩文件的工具类
 */

public class ZipUtil {

    private static String TAG = ZipUtil.class.getSimpleName();
    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    private ZipUtil() {}

    /**
     * @param sourceFolder 需压缩文件 或者 文件夹 路径
     * @param zipFilePath 压缩文件输出路径
     * @throws Exception
     */
    public static Observable<String> zipRx(final String sourceFolder,final String zipFilePath) throws Exception {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return zip(sourceFolder,zipFilePath);
            }
        });
    }

    /**
     * 压缩文件
     * @param sourceFolders 一组 压缩文件夹 或 文件
     * @param zipFilePath 压缩文件输出路径
     * @throws Exception
     */
    public static Observable<String> zipRx(final String[] sourceFolders,final String zipFilePath) throws Exception {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return zip(sourceFolders,zipFilePath);
            }
        });
    }

    /**
     * @param zipFileName 要解压的文件
     * @param outputDirectory 解压后的目录
     * @return
     */
    public static Observable<String> unZipRx(final String zipFileName,final String outputDirectory) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return unZip(zipFileName, outputDirectory);
            }
        });
    }

    /**
     * @param sourceFolder 需压缩文件 或者 文件夹 路径
     * @param zipFilePath 压缩文件输出路径
     * @throws Exception
     */
    private static String zip(String sourceFolder, String zipFilePath) throws Exception {
        LogUtil.d(TAG,"开始压缩 ["+sourceFolder+"] 到 ["+zipFilePath+"]");
        OutputStream out = new FileOutputStream(zipFilePath);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        ZipOutputStream zos = new ZipOutputStream(bos);
        File file = new File(sourceFolder);
        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }
        zipFile(file, basePath, zos);
        zos.closeEntry();
        zos.close();
        bos.close();
        out.close();
        return zipFilePath;
    }

    /**
     * 压缩文件
     * @param sourceFolders 一组 压缩文件夹 或 文件
     * @param zipFilePath 压缩文件输出路径
     * @throws Exception
     */
    private static String zip(String[] sourceFolders, String zipFilePath) throws Exception {
        OutputStream out = new FileOutputStream(zipFilePath);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        ZipOutputStream zos = new ZipOutputStream(bos);
        for (int i = 0; i < sourceFolders.length; i++) {
            LogUtil.d(TAG,"开始压缩 ["+sourceFolders[i]+"] 到 ["+zipFilePath+"]");
            File file = new File(sourceFolders[i]);
            String basePath = null;
            basePath = file.getParent();
            zipFile(file, basePath, zos);
        }
        zos.closeEntry();
        zos.close();
        bos.close();
        out.close();
        return zipFilePath;
    }

    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[CACHE_SIZE];
        for (File file : files) {
            if (file.isDirectory()) {
                LogUtil.d(TAG,"目录："+file.getPath());
                basePath = basePath.replace('\\', '/');
                if(basePath.substring(basePath.length()-1).equals("/")){
                    pathName = file.getPath().substring(basePath.length()) + "/";
                }else{
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                }

                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length()) ;
                pathName = pathName.replace('\\', '/');
                if(pathName.substring(0,1).equals("/")){
                    pathName = pathName.substring(1);
                }
                LogUtil.d(TAG,"压缩："+pathName);
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
    }

    /**
     * 解压zip文件
     * @param zipFileName
     * @param outputDirectory
     */
    private static String unZip(String zipFileName, String outputDirectory)
            throws Exception {
        LogUtil.d(TAG,"开始解压 ["+zipFileName+"] 到 ["+outputDirectory+"]");
        ZipFile zipFile = new ZipFile(zipFileName);
        try {
            Enumeration<?> e = zipFile.entries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                LogUtil.d(TAG,"解压：" + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    LogUtil.d(TAG,"创建目录：" + outputDirectory + File.separator + name);
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0,
                                fileName.lastIndexOf("/")));
                        fileName = fileName.substring(
                                fileName.lastIndexOf("/") + 1,
                                fileName.length());
                    }
                    File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());
                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    in.close();
                    out.close();
                }
            }
            LogUtil.d(TAG,"解压 ["+zipFileName+"] 完成！");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            zipFile.close();
        }
        return outputDirectory;
    }

    /**
     * 创建目录
     * @author hezhao
     * @Time   2017年7月28日 下午7:10:05
     * @param directory
     * @param subDirectory
     */
    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true) {
                fl.mkdir();
            } else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

//    /**
//     * 无需解压直接读取Zip文件和文件内容
//     * @Time   2017年7月28日 下午3:23:10
//     * @param file 文件
//     * @throws Exception
//     */
//    public static void readZipFile(String file) throws Exception {
//        ZipFile zipFile = new ZipFile(file);
//        InputStream in = new BufferedInputStream(new FileInputStream(file));
//        java.util.zip.ZipInputStream zin = new java.util.zip.ZipInputStream(in);
//        java.util.zip.ZipEntry ze;
//        while ((ze = zin.getNextEntry()) != null) {
//            if (ze.isDirectory()) {
//            } else {
//                LogUtil.i(TAG,"file - " + ze.getName() + " : "
//                        + ze.getSize() + " bytes");
//                long size = ze.getSize();
//                if (size > 0) {
//                    BufferedReader br = new BufferedReader(
//                            new InputStreamReader(zipFile.getInputStream(ze)));
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        System.out.println(line);
//                    }
//                    br.close();
//                }
//                System.out.println();
//            }
//        }
//        zin.closeEntry();
//    }
}
