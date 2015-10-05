package com.company;

import java.io.*;
import java.net.Socket;

/**
 * Created by mialiu on 9/17/15.
 */
public class PeerServerThread implements Runnable {
    private Socket _socket = null;
    public PeerServerThread(Socket socket) {
        _socket = socket;
    }
    @Override
    public void run() {
        //PeerServer p1 = new PeerServer();
        //p1.Start();
        ProcessCommand();
    }

    protected void ProcessCommand() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            // "download a.txt"
            String OneCommand = input.readLine();
            int blank = OneCommand.indexOf(' ');
            String c1 = OneCommand.substring(0, blank);
            String c2 = OneCommand.substring(blank + 1);

            if (c1.equals("download")) {
                UploadFile(c2);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected boolean UploadFile (String file) {
        try {
            String fileName = Commander._folderPath + "/" + file;
            //System.out.println("Begin to Upload file " + fileName);
            DataInputStream fs = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            DataOutputStream ps = new DataOutputStream(_socket.getOutputStream());

            byte[] buf = new byte[8192];

            while (true) {
                int read = 0;
                if (fs != null) {
                    read = fs.read(buf);
                }

                //System.out.println(read + " bytes have been uploaded.");

                if (read == -1) {
                    break;
                }
                ps.write(buf, 0, read);
            }
            ps.flush();
            fs.close();
            _socket.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
