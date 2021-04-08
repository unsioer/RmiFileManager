package top.enatsu.server;

import java.rmi.server.*;
import java.io.*;
import java.net.*;

public class MyRMISocket
        extends RMISocketFactory {
    public Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        if (port == 0)
            port = ServerMain.localPort;

        System.out.println("RMI注册或数据传输端口: " + port);
        return new ServerSocket(port);
    }
}
