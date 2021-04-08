package top.enatsu.server.facade;

import org.apache.commons.io.FileUtils;
import top.enatsu.facade.FileManager;
import top.enatsu.server.ServerMain;
import top.enatsu.util.FileInfo;
import top.enatsu.util.FileInfoImpl;
import top.enatsu.util.FileTool;
import top.enatsu.util.ResponseImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileManagerImpl extends UnicastRemoteObject implements FileManager {
    public FileManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public String hello() {
        return "Hello, World!";
    }

    @Override
    public File getFile(String srcPath) {
        if (!FileTool.isPathLegal(srcPath)) {
            return null;
        }
        File file = new File(new File(ServerMain.root), srcPath);
        if (file.exists()) return file;
        else return null;
    }

    @Override
    public FileInfo getFileInfo(String srcPath) {
        if (!FileTool.isPathLegal(srcPath)) {
            return null;
        }
        File file = new File(new File(ServerMain.root), srcPath);
        FileInfo fileInfo=new FileInfoImpl();
        fileInfo.setInfo(file.getName(),"",file.isDirectory(),file.length());
        if (file.exists()) return fileInfo;
        else return null;
    }

    @Override
    public ResponseImpl getFileList(String srcDir) {
        if (!FileTool.isPathLegal(srcDir)) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File file = new File(new File(ServerMain.root), srcDir);
        ResponseImpl response = new ResponseImpl();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            FileTool.printFileInfo(files);
            response.setResponse(true, files, "成功");

        } else {
            response.setResponse(false, "");
        }
        return response;
    }

    @Override
    public ResponseImpl createFolder(String srcDir) {
        if (!FileTool.isPathLegal(srcDir)) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File file = new File(new File(ServerMain.root), srcDir);
        if (!file.exists()) {
            System.out.println(file.getAbsolutePath());
            if (file.mkdirs()) {
                return new ResponseImpl(true, "创建成功");
            }
            return new ResponseImpl(false, "创建失败");
        }
        return new ResponseImpl(false, "创建失败：已存在同名文件或文件夹");
    }

    @Override
    public ResponseImpl moveFile(String srcPath, String dstDir) {
        if (!FileTool.isPathLegal(srcPath) || !FileTool.isPathLegal(dstDir)) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File srcFile = new File(new File(ServerMain.root), srcPath);
        File dstFile = new File(new File(ServerMain.root), dstDir);
        if (!srcFile.exists()) {
            return new ResponseImpl(false, "文件或文件夹不存在");
        }
        if (dstFile.exists() && !dstFile.isDirectory()) {
            return new ResponseImpl(false, "目标路径不是文件夹");
        }
        try {
            FileUtils.moveToDirectory(srcFile, dstFile, true);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseImpl(false, e.toString());
        }
        return new ResponseImpl(true, "移动成功");
    }

    @Override
    public ResponseImpl copyFile(String srcPath, String dstDir) {
        if (!FileTool.isPathLegal(srcPath) || !FileTool.isPathLegal(dstDir)) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File srcFile = new File(new File(ServerMain.root), srcPath);
        File dstFile = new File(new File(ServerMain.root), dstDir);
        if (!srcFile.exists()) {
            return new ResponseImpl(false, "文件或文件夹不存在");
        }
        if (dstFile.exists()){
            if(!dstFile.isDirectory()) {
                return new ResponseImpl(false, "目标路径不是文件夹");
            }
        }
        if (dstFile.equals(srcFile)){
            return new ResponseImpl(false, "目标路径与源路径相同");
        }
        try {
            FileUtils.copyToDirectory(srcFile, dstFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseImpl(false, e.toString());
        }
        return new ResponseImpl(false, "复制成功");
    }

    @Override
    public ResponseImpl renameFile(String srcPath, String dstName) {
        if (!FileTool.isPathLegal(srcPath) || !FileTool.isFileNameLegal(dstName)) {
            return new ResponseImpl(false, "文件路径或名称不合法");
        }
        File srcFile = new File(new File(ServerMain.root), srcPath);
        if (!srcFile.exists()) {
            return new ResponseImpl(false, "文件或文件夹不存在");
        }
        File dstFile = new File(srcFile.getParentFile(), dstName);
        if (dstFile.exists()) {
            return new ResponseImpl(false, "目标文件或文件夹已存在");
        }
        System.out.println(srcFile.getAbsolutePath());
        System.out.println(dstFile.getAbsolutePath());
        if (srcFile.renameTo(dstFile)) {
            return new ResponseImpl(true, "重命名成功");
        } else {
            return new ResponseImpl(false, "重命名失败");
        }
    }


    @Override
    public ResponseImpl deleteFile(String srcPath) {
        if (!FileTool.isPathLegal(srcPath)) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File file = new File(new File(ServerMain.root), srcPath);
        if (file.equals(new File(ServerMain.root))) {
            return new ResponseImpl(false, "根目录不可删除");
        }
        if (!file.exists()) {
            return new ResponseImpl(false, "文件或文件夹不存在");
        }
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                return new ResponseImpl(true, "删除成功");
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseImpl(false, e.toString());
            }
        } else {
            System.out.println(file.getAbsolutePath());
            if (FileUtils.deleteQuietly(file)) {
                return new ResponseImpl(true, "删除成功");
            }
            return new ResponseImpl(false, "删除失败");
        }
    }

    @Override
    public ResponseImpl uploadFile(FileInfoImpl info) {
        if (!FileTool.isPathLegal(info.getPath())) {
            return new ResponseImpl(false, "文件路径不合法");
        }
        File dstDir = new File(new File(ServerMain.root), info.getPath());
        if (!dstDir.exists()) {
            if (!dstDir.mkdirs()) {
                return new ResponseImpl(false, "创建文件夹失败");
            }
        }
        File dstFile = new File(dstDir, info.getName());
        BufferedOutputStream outputStream;
        try {
            if (dstFile.exists()) {
                return new ResponseImpl(false, "文件已存在");
            } else {
                if (!dstFile.createNewFile()) {
                    return new ResponseImpl(false, "创建文件失败");
                }
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(dstFile));
            outputStream.write(info.getContent());
            outputStream.close();
            if (dstFile.exists()) {
                return new ResponseImpl(true, "上传成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseImpl(false, e.toString());
        }
        return new ResponseImpl(false, "上传失败");
    }

}
