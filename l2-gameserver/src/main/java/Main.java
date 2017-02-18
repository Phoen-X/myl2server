import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Phoen-X on 17.02.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        ByteBuffer buf = ByteBuffer.allocate(48);

        buf.clear();
        buf.put("Hello".getBytes());

        buf.flip();
        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }

        socketChannel.close();


    }
}
