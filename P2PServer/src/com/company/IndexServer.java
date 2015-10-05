package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mialiu on 9/17/15.
 */
public class IndexServer {
    public static List<PeerNode> allPeers = new ArrayList<PeerNode>();

    public void Run() {
        try {
            int port = 8181;
            ServerSocket listener = new ServerSocket();
            //System.out.println("Listening port " + listener.getLocalPort());
            listener.bind(new InetSocketAddress(Inet4Address.getByName("0.0.0.0"), port));
            System.out.println("Start listening: " + listener.getInetAddress());
            while (true){
                Socket socket = listener.accept();
                // start a thread
/*                System.out.println(socket.getRemoteSocketAddress().toString());
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Server: " + input.readLine());
*/
                HandlingOnePeerThread thread1 = new HandlingOnePeerThread(socket);
                Thread thread2 = new Thread(thread1);
                thread2.start();
                // Echo
/*                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                output.write("World!" + port);
                output.flush();
*/
                //socket.close();
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
