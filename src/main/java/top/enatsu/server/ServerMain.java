package top.enatsu.server;

import top.enatsu.facade.FileManager;
import top.enatsu.server.facade.FileManagerImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerMain {

    public static String root = "";

    public static void main(String[] args) {
        root = "E:\\testFolder";
        if (args[0] != null) {
            root = args[0];
        }
        try {
            FileManager fileManager = new FileManagerImpl();

            LocateRegistry.createRegistry(5000);

            Context namingContext = new InitialContext();

            namingContext.rebind("rmi://0.0.0.0:5000/file", fileManager);

            System.out.println("======= 启动RMI服务成功! =======");

        } catch (RemoteException | NamingException e) {
            e.printStackTrace();
        }

    }

}
