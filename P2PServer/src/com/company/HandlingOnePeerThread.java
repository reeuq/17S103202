package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mialiu on 9/17/15.
 */
public class HandlingOnePeerThread implements Runnable  {
    private Socket _socket = null;
    //private List<PeerNode> _peerNodes = new ArrayList<PeerNode>();
    private PeerNode _node = null;
    //private int _nodeIndex = -1;

    public HandlingOnePeerThread (Socket socket) {
        _socket = socket;
    }

    @Override
    public void run() {
    //PeerServer p1 = new PeerServer();
    //p1.Start();{
        System.out.println(_socket.getRemoteSocketAddress().toString());
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            while (true) {

                String OneCommand = input.readLine();
                System.out.println("IndexServer: " + OneCommand);
                if ( null == OneCommand || OneCommand.isEmpty() ) {
                    break;
                }
                int blank = OneCommand.indexOf(' ');
                String c1 = OneCommand.substring(0, blank);
                String c2 = OneCommand.substring(blank + 1);
                //String[] args = OneCommand.split(" ");

                if (c1.equals("register")) {
                    Register(c2 );
                } else if (c1.equals("addfiles")){
                    AddFiles(c2 );
                } else if (c1.equals("search")) {
                    SearchFile(c2 );
                } else {
                    HandleUnknown();
                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            Unregister();
        }

        // Echo
        //BufferedWriter output = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
        //output.write("World!" + port);
        //output.flush();

        //_socket.close();
    }

    protected boolean Register(String args) {
        // A sample for args: {"register", "5555"}, the number is the port
        CreateANewPeer(args);

        //if(IndexServer.allPeers.isEmpty()) {
            //CreateANewPeer(args[1]);
            IndexServer.allPeers.add(_node);
            //_nodeIndex++;
            return true;
        //}



        //return false;
    }

    protected boolean Unregister() {
        if (null == _node) {
            return true;
        }

        if (IndexServer.allPeers.isEmpty())
            return true;

        for (PeerNode iNode: IndexServer.allPeers) {
            //List<String> list = iNode._filePathes;
            if (iNode._id.equals(_node._id)) {
                IndexServer.allPeers.remove(iNode);
                return true;
            }
        }

        return false;
    }

    private String GetIPString(String ip) {
        //String intetIp = _socket.getInetAddress().toString();
        int slash = ip.indexOf('/');
        if(-1 != slash) {
            ip= ip.substring(slash + 1);
        }
        return ip;
    }

    private String GetPeerServerAddress(String ip, String port) {
/*        String intetIp = _socket.getInetAddress().toString();
        int slash = intetIp.indexOf('/');
        if(-1 != slash) {
            intetIp= intetIp.substring(slash + 1);
        }
        */
        return GetIPString(ip) + ":" + port;
    }

    private String GetPeerServerAddress(String ip, int port) {
        return GetPeerServerAddress(ip, String.valueOf(port));
    }

    private void CreateANewPeer(String port) {
        //PeerNode aNode = new PeerNode();

        _node = new PeerNode();
        _node._portToIndexServer = _socket.getPort();
        _node._portToPeers = Integer.parseInt(port);
        _node._ip = GetIPString(_socket.getInetAddress().toString());
        _node._id = _node._ip + ":" + port;// GetPeerServerAddress(_socket.getInetAddress().toString(), _socket.getPort());

        //_peerNodes.add(aNode);
        //IndexServer.allPeers.add(_node);
    }

    protected boolean AddFiles(String args) {
        // {"addfiles", "a.txt|b.txt"}
        if (_node != null) {
            String[] files = args.split("/");
            if (null == files) {
                return false;
            }
            _node._filePathes = Arrays.asList(files);
            return true;
        }
        return false;
    }

    protected boolean SearchFile(String args) {
        // {"search", "a.txt"}
        if (IndexServer.allPeers.isEmpty())
            return false;

        List<String> peerCollection = new ArrayList<String>();

        for (PeerNode iNode: IndexServer.allPeers) {
            List<String> list = iNode._filePathes;
            if (list != null && !list.isEmpty()) {
                if (-1 != list.indexOf(args)) {
                    peerCollection.add(iNode._id);
                }
            }
        }

        if (peerCollection.isEmpty()) {
            System.out.println("Whoops! Nothing has been found.");
        } else {
            System.out.println("You can find the file on below peers:");
            for (String iStr : peerCollection) {
                System.out.println(iStr);
            }
            System.out.println();
        }
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            //output.write("World!" + port);
            for (String id : peerCollection) {
                output.write(id + "/");
            }
            output.newLine();
            output.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    private int GetCurrentNodeIndex() {
        if (IndexServer.allPeers.isEmpty())
            return -1;
        if (null == _node)
            return -2;
        return IndexServer.allPeers.indexOf(_node);
    }

    protected void HandleUnknown() {
        System.out.println("Currently, \"register\", \"files\", and \"search\" are the only supported commands.");
    }
}
