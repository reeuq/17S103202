package com.company;

import java.io.*;
import java.net.Socket;

/**
 * Created by mialiu on 9/17/15.
 */
public class PeerClient {
    private Socket _socket = null;
    private String _path = null;

    public PeerClient() {
        _path = Commander._folderPath;
    }

    public PeerClient(String path) {
        _path = path;
    }

    public boolean Connect(String host, int port, String file){
        try {
            _socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            //out.println("Hello");

            // Send download request
            // A sample: "download a.txt"
            out.println("download " + file);


            //
/*            BufferedReader input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            //System.out.println("Received: " + input.readLine());
            String OneCommand = input.readLine();
            if (null == OneCommand || OneCommand.isEmpty()) {
                return false;
            }
            if (OneCommand.equals("y")) {
                return true;
            }
*/
            //_socket.close();
            return true;
        } catch (Exception e){
            //System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean Download(String ip, String file) {
        //System.out.println("In Download method");
        if (ip.isEmpty() || file.isEmpty()) {
            return false;
        }
        String[] args = ip.split(":");
        if ( Connect(args[0], Integer.parseInt(args[1]), file)) {
            return ProcessFile(file);
        }
        return false;
    }

    protected boolean ProcessFile(String file) {
        String newFileName = _path + "/" + file;
        try {
            //System.out.println("Begin to download the file " + newFileName);
            DataInputStream is = new DataInputStream(_socket.getInputStream());//(new BufferedInputStream(new FileInputStream(newFileName)));
            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(newFileName)));
            int read = 0;
            byte[] buffer = new byte[8092];

            while (true) {
                if (is != null) {
                    read = is.read(buffer);
                }

                //System.out.println(read + " bytes have been downloaded.");
                if (-1 == read) {
                    break;
                }

                fileOut.write(buffer, 0, read);
            }
            fileOut.close();
            _socket.close();
            //System.out.println(file + " has been downloaded!");
            return true;
        } catch (Exception ex) {
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

/*    protected boolean IsFileAccessible(String ip, String file) {
        if (ip.isEmpty() || file.isEmpty()) {
            return false;
        }

        return false;
    }*/

}

