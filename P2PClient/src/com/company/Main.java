package com.company;

public class Main {

    public static void main(String[] args) {
        // Background Thread
        PeerServer ps1 = new PeerServer();
        ps1.Start();

        // Main Thread
        Commander c1 = new Commander(ps1);
        c1.Run();
        ps1.Stop();
        //System.out.println("Last!");

        // Testing code

/*        TestCase tc = new TestCase();
        tc.CreateFiles1000();

        tc.Register1000();
        tc.Search1000();
        tc.GetFile1000();
        tc.Clean();
*/
    }
}

