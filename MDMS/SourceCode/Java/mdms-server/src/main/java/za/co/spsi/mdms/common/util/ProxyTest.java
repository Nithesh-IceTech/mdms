package za.co.spsi.mdms.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Created by jaspervdbijl on 2017/05/19.
 */
public class ProxyTest {

    public static class Channel extends Thread {
        private InputStream is;
        private OutputStream os;
        private boolean closed = false;

        public Channel(Socket s1,Socket s2) throws IOException {
            this.is = s1.getInputStream();
            this.os = s2.getOutputStream();
        }

        public void run() {
            try {
                for (int b = is.read();b != -1;b = is.read()) {
                    os.write(b);
                    System.out.print((char)b);
                }
            } catch (Exception ex) {
                closed = true;
            }
        }

        public boolean isClosed() {
            return closed;
        }

    }
    public static class Test extends Thread {
        private Socket socket;
        private Socket out;
        private Channel[] channels;

        public Test(Socket socket) {
            this.socket =  socket;
        }


        private void closeSocket(Socket socket) {
            try {
                socket.close();
            } catch (IOException e) {}
        }
        public void run() {
            try {
                try {
                    out = new Socket("196.213.214.250",443);
                    this.channels = new Channel[]{new Channel(socket,out),new Channel(out,socket)};
                    Arrays.stream(channels).forEach(c -> c.start());
                    while (!Arrays.stream(channels).filter(c -> c.isClosed()).findAny().isPresent()) {
                        Thread.sleep(100);
                    }
                } catch (Exception se) {
                    se.printStackTrace();
                }
            } finally {
                closeSocket(socket);
                closeSocket(out);
            }
        }

    }
    public static void main(String args[]) throws Exception {
        ServerSocket ss = new ServerSocket(9090);
        ss.setSoTimeout(1000);
        while (true) {
            try {
                new Test(ss.accept()).start();
            } catch (SocketTimeoutException ste) {}
        }
    }
}
