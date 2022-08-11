package correcter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncoderEightThree implements Encoder {
    private static int reservedByte = 0;
    private static int helpCounter = 0;

    @Override
    public void encode(String fileFrom, String fileTo) {
        try (FileInputStream fileInputStream = new FileInputStream(fileFrom);
             FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            while (fileInputStream.available() > 0) {
                int encodedByte;
                if (helpCounter < 3) {
                    int byteToEncode = fileInputStream.read();
                    reservedByte = (reservedByte << 8) | byteToEncode;
                    helpCounter += 8;
                }
                encodedByte = encodeByte(reservedByte);
                fileOutputStream.write(encodedByte);
            }

            while (helpCounter > 0) {
                int encodedByte = encodeByte(reservedByte);
                fileOutputStream.write(encodedByte);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private int encodeByte(int byteToEncode) {
        int counter = 0;
        int checker = 1 << helpCounter - 1;
        int byteEncoded = 0;

        while (counter < 3) {
            counter++;
            if ((byteToEncode & checker) > 0) {
                byteToEncode -= checker;
                for (int i = 0; i < 2; i++) {
                    byteEncoded = byteEncoded << 1;
                    byteEncoded++;
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    byteEncoded = byteEncoded << 1;
                }
            }
            checker = checker >>> 1;
        }

        byteEncoded = byteEncoded << 2;
        byteEncoded = makeXORBits(byteEncoded);

        reservedByte = byteToEncode;
        helpCounter -= counter;

        return byteEncoded;
    }

    private int makeXORBits(int byteToEncode) {
        int firstBit = (byteToEncode & 0b10000000) >>> 7;
        int secondBit = (byteToEncode & 0b100000) >> 5;
        int thirdBit = (byteToEncode & 0b1000) >> 3;
        int XORBit = firstBit ^ secondBit ^ thirdBit;
        int XOR2Bits = (XORBit << 1) | XORBit;

        return byteToEncode | XOR2Bits;
    }
}
