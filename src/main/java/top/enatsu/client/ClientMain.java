package top.enatsu.client;

import top.enatsu.facade.FileManager;
import top.enatsu.util.FileInfoImpl;
import top.enatsu.util.FileTool;
import top.enatsu.util.ResponseImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String url = "rmi://127.0.0.1:5000/file";
        if (args[0] != null) {
            url = args[0];
        }
        try {
            Context namingContext = new InitialContext();
            FileManager fileManager = (FileManager) namingContext.lookup(url);
            System.out.println(fileManager.hello());
            Scanner input = new Scanner(System.in);
            String command;
            ResponseImpl response = null;
            while (true) {
                command = input.nextLine();
                switch (command) {
                    case "ls": {
                        System.out.print("请输入要查看的文件夹路径（缺省为根目录）：");
                        String path = input.nextLine();
                        File[] files = fileManager.getFileList(path).getFiles();
                        if (files != null) {
                            FileTool.printFileInfo(files);
                        } else System.out.println("命令出错啦");

                        break;
                    }
                    case "mkdir":
                        System.out.print("请输入要创建的文件夹路径：");
                        String pathFolderName = input.nextLine();
                        response = fileManager.createFolder(pathFolderName);
                        System.out.println(response.getMessage());
                        break;
                    case "rm": {
                        System.out.print("请输入要删除的文件或文件夹路径：");
                        String path = input.nextLine();
                        response = fileManager.deleteFile(path);
                        System.out.println(response.getMessage());
                        break;
                    }
                    case "mv": {
                        System.out.print("请输入要移动的源文件或文件夹路径：");
                        String srcPath = input.nextLine();
                        System.out.print("请输入要移动到的目标文件或文件夹路径：");
                        String dstDir = input.nextLine();
                        response = fileManager.moveFile(srcPath, dstDir);
                        System.out.println(response.getMessage());
                        break;
                    }
                    case "cp": {
                        System.out.print("请输入要复制的源文件或文件夹路径：");
                        String srcPath = input.nextLine();
                        System.out.print("请输入要粘贴到的目标文件或文件夹路径：");
                        String dstDir = input.nextLine();
                        response = fileManager.copyFile(srcPath, dstDir);
                        System.out.println(response.getMessage());
                        break;
                    }
                    case "ren": {
                        System.out.print("请输入要重命名的源文件或文件夹路径：");
                        String srcPath = input.nextLine();
                        System.out.print("请输入新的文件或文件夹名：");
                        String dstName = input.nextLine();
                        response = fileManager.renameFile(srcPath, dstName);
                        System.out.println(response.getMessage());
                        break;
                    }
                    case "upload": {
                        System.out.print("请输入要上传的文件路径：");
                        String srcPath = input.nextLine();
                        File srcFile = new File(srcPath);
                        if (!srcFile.exists()) {
                            System.out.println("文件不存在");
                        } else if (!srcFile.isFile()) {
                            System.out.println("上传的不是文件");
                        } else {
                            System.out.print("请输入要上传的目标文件夹：");
                            String dstDir = input.nextLine();

                            byte[] content = new byte[(int) srcFile.length()];
                            BufferedInputStream inputStream = null;
                            try {
                                inputStream = new BufferedInputStream(new FileInputStream(srcFile));
                                inputStream.read(content);
                                FileInfoImpl info = new FileInfoImpl();
                                info.setInfo(srcFile.getName(), dstDir, content);
                                response = fileManager.uploadFile(info);
                                System.out.println(response.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        break;
                    }
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }

        } catch (RemoteException | NamingException e) {
            e.printStackTrace();
        }


    }
}
