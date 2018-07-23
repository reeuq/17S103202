package com.company;

public class Main {

    public static void main(String[] args) {
        // 加 buffer 可调节同时起 thread 数量
        IndexServer server = new IndexServer();
        server.Run();
    }
}
