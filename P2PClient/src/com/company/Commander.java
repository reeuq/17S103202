package com.company;

//import java.util.Scanner;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

/**
 * Created by mialiu on 9/17/15.
 */
public class Commander {
    private PeerServer _peerServer = null;
    private PeerClient _peerClient = null;

    public FileWriter outputFile = null;

    public static String _folderPath = null;

    public Commander(PeerServer ps) {
        _peerServer = ps;
    }

    public void Run(){
        Scanner s1 = new Scanner(System.in);
        //while (true) {

            System.out.println("Please input the server host");
            String host = s1.nextLine();
            //String host = "localhost";
            System.out.println("Please input the port");
            int port = Integer.parseInt(s1.nextLine());
            //int port = 8181;

            //String str1 = s1.nextLine();

/*            try {
                //int port = Integer.parseInt(str1);

                _peerClient = new PeerClient();

                //System.out.println("Now connecting...");

                //p1.Connect("127.0.0.1", port);
                _peerClient.Connect(host, port);

            } catch (Exception e){
                e.printStackTrace();
            }
*/
            System.out.println("Please input the sharing path (without the slash on the back)");
            String path = s1.nextLine();
            //String path = "/Users/mialiu/Documents ";
        // /Users/mialiu/Desktop
        _folderPath = path;

        IndexServerClient client = new IndexServerClient(_peerServer._port);
        boolean result = false;
        try {
            result = client.Register(host, port);
            result = client.SendFileList(path);
            } catch (Exception e){
                e.printStackTrace();
            }

        try {
            outputFile = new FileWriter(path + "/output.txt", true);
            if (result){
                outputFile.write("Register to the server successfully.\n");
            } else {
                outputFile.write("Register to the server unsuccessfully.\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        while(true) {
            System.out.println("Please input the file name for search");
            //String name = "Lecture3.pdf";
            String name = s1.nextLine();
            List<String> list = client.Search(name);
            //}

            if (list == null || list.isEmpty()) {
                try {
                    outputFile.write("Search result: no peers contain " + name + ".\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    outputFile.write(name + " has been found on peers:\n");
                    for (String ip : list) {
                        outputFile.write("    " + ip + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("Download the file? (y|n)");
                //String ans = "y";
                String ans = s1.nextLine();
                if (ans.equals("y") || ans.equals("Y")) {
                    _peerClient = new PeerClient();
                    System.out.println("Try to download from " + list.get(0));
                    result = _peerClient.Download(list.get(0), name);
                    try {
                        if (result) {
                            outputFile.write(name + " has been downloaded.\n");
                        } else {
                            outputFile.write(name + " has not been downloaded successfully.\n");
                        }
                        outputFile.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }
//        client.Close();
            //System.out.println("");
            //System.out.println("");
        //}
    }
}
