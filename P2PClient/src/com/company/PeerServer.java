package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by mialiu on 9/17/15.
 */
public class PeerServer implements Runnable {
    public int _port = 0;
    private ServerSocket _listener = null;

    public PeerServer() {
        _port = new Random().nextInt(10000) + 40000;
    }

    public void Start() {

        Thread t1 = new Thread(this);
        t1.start();
    }

    public void Stop() {
        try {
            _listener.close();
        } catch (Exception e) {
            //System.out.println("Error: " + e.getMessage());
        }

    }

    public void run() {
        try {
            _listener = new ServerSocket(_port);
            //System.out.println("Listening port " + _listener.getLocalPort());
            while (true) {
                Socket socket = _listener.accept();

                PeerServerThread thread1 = new PeerServerThread(socket);
                Thread thread2 = new Thread(thread1);
                thread2.start();

                //ProcessCommand(socket);


                //
/*                System.out.println(socket.getRemoteSocketAddress().toString());
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Server: " + input.readLine());
                System.out.println("getInetAddress().: "+socket.getInetAddress().toString());
                System.out.println("getLocalAddress().: " + socket.getLocalAddress().toString());
                System.out.println("getLocalSocketAddress().: " + socket.getLocalSocketAddress().toString());
                System.out.println("getPort().: " + socket.getPort());
                System.out.println("getLocalPort().: " + socket.getLocalPort());
                System.out.println("getRemoteSocketAddress()" + socket.getRemoteSocketAddress().toString());

                // Echo
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                output.write("World!" + _port);
                output.flush();

                socket.close();
*/
            }
        } catch (Exception e) {
            //System.out.println("Error: " + e.getMessage());
        }
    }
}

