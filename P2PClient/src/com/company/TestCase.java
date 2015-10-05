package com.company;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by mialiu on 9/21/15.
 */
public class TestCase {
    private PeerServer _peerServer = new PeerServer();
    private IndexServerClient _indexClient = new IndexServerClient(_peerServer._port);
    private PeerClient _peerClient = new PeerClient();
    private Commander _commander = new Commander(_peerServer);

    public void CreateFiles1000() {
        String path = "/Users/mialiu/Documents/test/";
        try {
            for (int i = 0; i < 1000; i++) {
                FileWriter outputFile = new FileWriter(path + i + ".txt", true);
                for (int j = 0; j < i; j++) {
                    outputFile.write("blah blah\n");
                }
                outputFile.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Register1000() {
        String host = "localhost";
        int port = 8181;
        String path = "/Users/mialiu/Documents/test";
        _commander._folderPath = path;
        Date start = new Date();
        try {
            //for (int i = 0; i < 1000; i++) {
            //PeerServer peerServer = new PeerServer();
            //IndexServerClient client = new IndexServerClient(peerServer._port);
            boolean result = false;
            result = _indexClient.Register(host, port);
            result = _indexClient.SendFileList(path);
            _peerServer.Start();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println("Register 1000 files needs " + (end.getTime() - start.getTime()) + " ms.");

    }

    public void Search1000() {
        String path = "/Users/mialiu/Documents/test";
        Date start = new Date();
        try {
            for (int i = 0; i < 1000; i++) {
                _indexClient.Search(i + ".txt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println("Search 1000 files needs " + (end.getTime() - start.getTime()) + " ms.");
    }

    public void GetFile1000() {
        String host = "localhost";
        int port = 8181;
        String path = "/Users/mialiu/Documents/test1";

        PeerServer peerServer = new PeerServer();
        IndexServerClient indexClient = new IndexServerClient(peerServer._port);
        PeerClient peerClient = new PeerClient(path);
        Commander commander = new Commander(peerServer);

        indexClient.Register(host, port);
        indexClient.SendFileList(path);

        //String path = "/Users/mialiu/Documents/test";
        List<String> list = indexClient.Search("1.txt");

        // Download files in 5k
        Date start = new Date();
        for (int i = 0; i < 1000; i++) {
            Date start0 = new Date();
            peerClient.Download(list.get(0), "450.txt");
            Date end0 = new Date();
            System.out.println( end0.getTime() - start0.getTime() + "    " );
        }
        Date end = new Date();
        System.out.println("Download 1000 files needs " + (end.getTime() - start.getTime()) + " ms.");

        peerServer.Stop();
    }

    public void Clean() {
        _peerServer.Stop();

        String path = "/Users/mialiu/Documents/test/";
        try {
            for (int i = 0; i < 1000; i++) {
                File f = new File("/Users/mialiu/Documents/test/" + i + ".txt");
                f.delete();
            }
            //File dir = new File(path);
            //dir.delete();

            for (int i = 0; i < 1000; i++) {
                File f = new File("/Users/mialiu/Documents/test1/" + i + ".txt");
                f.delete();
            }
            //File dir1 = new File(path);
            //dir.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
