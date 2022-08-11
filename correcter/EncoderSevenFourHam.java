package correcter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncoderSevenFourHam implements Encoder {
    @Override
    public void encode(String fileFrom, String fileTo) {
        try (FileInputStream fileInputStream = new FileInputStream(fileFrom);
             FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            while (fileInputStream.available() > 0) {
                int byteToEncode = fileInputStream.read();
                int[] encodedBytes = hamEncode(byteToEncode);
                for (int i = 0; i < 2; i++) {
                    fileOutputStream.write(encodedBytes[i]);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    private int[] hamEncode(int byteToEncode) {
        int[] encodedBytes = new int[2];
        encodedBytes[0] = significantBits((byteToEncode >>> 4));
        encodedBytes[1] = significantBits(byteToEncode);

        for (int index = 0; index < encodedBytes.length; index++) {
            for (int i = 0; i < 3; i++) {
                int shifter = (int) Math.pow(2, i);
                int result = 0;

                for (int j = 0; j < 8; j++) {
                    if (j == 0) {
                        j += shifter;
                    } else if (j % shifter == 0) {
                        j += shifter;
                    }
                    int tempBit = 1 & (encodedBytes[index] >> (8 - j));
                    result = result ^ tempBit;
                }
                encodedBytes[index] += result << (8 - shifter);
            }
        }
        return encodedBytes;
    }

    private int significantBits(int byteToEncode) {
        int byteWithSignificantBits = 0;
        for (int j = 0; j < 4; j++) {
            int checker = 1 << j;
            if ((byteToEncode & checker) > 0) {
                switch (j) {
                    case 0:
                        byteWithSignificantBits += 0b10;
                        break;
                    case 1:
                        byteWithSignificantBits += 0b100;
                        break;
                    case 2:
                        byteWithSignificantBits += 0b1000;
                        break;
                    case 3:
                        byteWithSignificantBits += 0b100000;
                        break;
                }
            }
        }
        return byteWithSignificantBits;
    }
}
