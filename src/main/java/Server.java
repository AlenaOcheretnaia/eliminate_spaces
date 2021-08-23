import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {

        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23334));
        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                StringBuilder resultMessage = new StringBuilder();
                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    resultMessage.append(clearSpaces(msg));
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);
                    socketChannel.write(ByteBuffer.wrap((clearSpaces(msg)).getBytes(StandardCharsets.UTF_8)));
                }
                System.out.println("Result: " + resultMessage);
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public static String clearSpaces(String msg) {
        return msg.replaceAll("\\s", "");
    }
}
