package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mialiu on 9/17/15.
 */
public class IndexServerClient {
    public PathScanner _scanner = null;
    public int _peerServerPort = 0;
    private Socket _socket = null;
    private PrintWriter _out = null;

    public IndexServerClient (int serverPort) {
        _peerServerPort = serverPort;
    }

    public boolean Connect (String serverHost, int port) {
        try {
            _socket = new Socket(serverHost, port);
            _out = new PrintWriter(_socket.getOutputStream(), true);

            return true;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean Register (String serverHost, int port) {
        if (Connect(serverHost, port)) {
            try {
                //PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);

                // Send register
                // A sample: "register 5555", the number is the port
                _out.println("register " + Integer.toString(_peerServerPort));
                //_out.flush();

                //
                //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //System.out.println("Received: " + input.readLine());


                //_socket.close();
                return true;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                //return false;
            }
        }
        return false;
    }

    public boolean SendFileList(String folderPath) {
        _scanner = new PathScanner(folderPath);
        List<String> files = _scanner.ListAllFiles();
        try {
            //PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            // Send addfiles "addfiles a.txt/b.txt"
            _out.print("addfiles ");
            for (String file: files) {
                _out.print(file + "/");
            }
            _out.println();
            //_out.flush();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public List<String> Search(String fileName) {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            // "search a.txt"
            out.println("search " + fileName);
            out.flush();

            BufferedReader input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            String OneCommand = input.readLine();
            String[] lists = OneCommand.split("/");
            List<String> ipList = Arrays.asList(lists);

            if (OneCommand.isEmpty() || lists.length == 0) {
                ipList.clear();
                //System.out.println(ipList.size() + "Whoops! Nothing has been found.");
            } else {
                //System.out.println("You can find the file on below peers:");
                //for (String ip: ipList) {
                //    System.out.println(ip);
                //}
                //System.out.println();
            }
            return ipList;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void Close() {
        try {
            _socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.println("Session closed!");
    }
}
