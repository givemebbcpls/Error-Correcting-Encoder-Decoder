package correcter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Welcome {
    private static final Encoder encoder = new EncoderSevenFourHam();
    private static final Destructor destructor = new Destructor();
    private static final Decoder decoder = new DecoderSevenFourHam();
    static final String SEND_FILE = "send.txt";
    static final String ENCODED_FILE = "encoded.txt";
    static final String RECEIVED_FILE = "received.txt";
    static final String DECODED_FILE = "decoded.txt";

    public static void welcome() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Write a mode: ");
            String line = bufferedReader.readLine();
            switch (line) {
                case "encode":
                    encoder.encode(SEND_FILE, ENCODED_FILE);
                    break;
                case "send":
                    destructor.send(ENCODED_FILE, RECEIVED_FILE);
                    break;
                case "decode":
                    decoder.decode(RECEIVED_FILE, DECODED_FILE);
                    break;
            }
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
}
