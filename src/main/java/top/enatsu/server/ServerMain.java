package top.enatsu.server;

import top.enatsu.facade.FileManager;
import top.enatsu.server.facade.FileManagerImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.IOException;
import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;

public class ServerMain {

    public static String root = "";

    public static String hostname = "0.0.0.0";

    public static Integer port = 5000;

    public static Integer localPort = 4999;


    public static void main(String[] args) {
        root = "E:\\testFolder";
        if (args.length > 0 && args[0] != null) {
            root = args[0];
        }
        if (args.length > 1 && args[1] != null) {
            hostname = args[1];
        }
        System.setProperty("java.rmi.server.hostname", hostname);
        if (args.length > 2 && args[2] != null) {
            port = Integer.parseInt(args[2]);
        }
        try {
            RMISocketFactory.setSocketFactory(new MyRMISocket());

            Registry registry = LocateRegistry.createRegistry(port);

            FileManager fileManager = new FileManagerImpl();

            registry.rebind("file", fileManager);

            Context namingContext = new InitialContext();

            System.out.println(Inet4Address.getLocalHost().getHostAddress());
            namingContext.rebind("rmi://" + Inet4Address.getLocalHost().getHostAddress() + ":" + port + "/file", fileManager);

            System.out.println("======= 启动RMI服务成功! =======");

        } catch (IOException | NamingException e) {
            e.printStackTrace();
        }

    }

}
