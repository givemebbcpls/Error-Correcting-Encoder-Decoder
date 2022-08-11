package correcter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecoderSevenFourHam implements Decoder {
    @Override
    public void decode(String fileFrom, String fileTo) {
        try (FileInputStream fileInputStream = new FileInputStream(fileFrom);
             FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            while (fileInputStream.available() > 0) {
                int[] bytesToDecode = new int[2];
                for (int i = 0; i < bytesToDecode.length; i++) {
                    bytesToDecode[i] = fileInputStream.read();
                }

                int decodedByte = hamDecode(bytesToDecode);
                fileOutputStream.write(decodedByte);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    private int hamDecode(int[] bytesToDecode) {
        repairBytes(bytesToDecode);
        return assembleByte(bytesToDecode);
    }

    private int[] repairBytes(int[] bytesToDecode) {
        for (int index = 0; index < bytesToDecode.length; index++) {
            int shifterForWrongByte = 0;
            for (int i = 0; i < 3; i++) {
                int shifter = (int) Math.pow(2, i);
                int result = 0;

                for (int j = shifter + 1; j < 8; j++) {
                    if (j % shifter == 0) {
                        j += shifter;
                    }
                    int tempBit = 1 & (bytesToDecode[index] >>> (8 - j));
                    result = result ^ tempBit;
                }
                int byteToCheck = (bytesToDecode[index] >>> (8 - shifter)) & 1;
                if (byteToCheck != result) {
                    shifterForWrongByte += shifter;
                }
            }
            int wrongByte = 1 << (8 - shifterForWrongByte);
            if ((wrongByte & bytesToDecode[index]) > 0) {
                bytesToDecode[index] -= wrongByte;
            } else {
                bytesToDecode[index] += wrongByte;
            }
        }
        return bytesToDecode;
    }

    private int assembleByte(int[] bytesToDecode) {
        int decodedByte = 0;
        for (int i = 0; i < bytesToDecode.length; i++) {
            if (i > 0) {
                decodedByte = decodedByte << 4;
            }
            decodedByte += (bytesToDecode[i] & 0b10) >>> 1;
            decodedByte += (bytesToDecode[i] & 0b100) >>> 1;
            decodedByte += (bytesToDecode[i] & 0b1000) >>> 1;
            decodedByte += (bytesToDecode[i] & 0b100000) >>> 2;
        }
        return decodedByte;
    }
}
