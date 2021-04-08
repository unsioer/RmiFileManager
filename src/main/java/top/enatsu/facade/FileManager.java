package top.enatsu.facade;

import top.enatsu.util.FileInfo;
import top.enatsu.util.FileInfoImpl;
import top.enatsu.util.ResponseImpl;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileManager extends Remote {

    String hello() throws RemoteException;

    File getFile(String srcPath) throws RemoteException;

    FileInfo getFileInfo(String srcPath) throws RemoteException;

    ResponseImpl getFileList(String srcDir) throws RemoteException;

    ResponseImpl createFolder(String srcDir) throws RemoteException;

    ResponseImpl moveFile(String srcPath, String dstDir) throws RemoteException;

    ResponseImpl copyFile(String srcPath, String dstDir) throws RemoteException;

    ResponseImpl renameFile(String srcPath, String dstName) throws RemoteException;

    ResponseImpl deleteFile(String srcPath) throws RemoteException;

    ResponseImpl uploadFile(FileInfoImpl info) throws RemoteException;
}
