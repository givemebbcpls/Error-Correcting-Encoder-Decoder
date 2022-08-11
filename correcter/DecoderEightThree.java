package correcter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecoderEightThree implements Decoder {
    private static int reservedByte = 0;
    private static int helpCounter = 0;

    @Override
    public void decode(String fileFrom, String fileTo) {
        try (FileInputStream fileInputStream = new FileInputStream(fileFrom);
             FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            while (fileInputStream.available() > 0) {
                int decodedByte;
                while (helpCounter < 8) {
                    int byteToDecode = fileInputStream.read();
                    reservedByte = (reservedByte << 3) | repairByte(byteToDecode);
                    helpCounter += 3;
                }
                decodedByte = reservedByte >>> (helpCounter - 8);
                reservedByte -= decodedByte << (helpCounter - 8);
                fileOutputStream.write(decodedByte);
                helpCounter -= 8;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    private int repairByte(int brokeByte) {
        if ((brokeByte & 1) != ((brokeByte >> 1) & 1)) {
            return threeCleanBits(brokeByte);
        }
        int repairBit = findBit(brokeByte);
        return threeCleanBits(brokeByte ^ repairBit);
    }

    private int threeCleanBits(int brokeByte) {
        int threeCleanBits = 0;
        for (int i = 7; i > 2; i -= 2) {
            threeCleanBits = threeCleanBits << 1;
            threeCleanBits = threeCleanBits | (1 & (brokeByte >>> i));
        }
        return threeCleanBits;
    }

    private int findBit(int brokeByte) {
        boolean isFirstRight = whereRightBit(brokeByte);
        int rightBit = 1;
        int checker = 1;
        for (int i = 2; i < 7; i += 2) {
            checker = checker << i;
            int firstBit = (brokeByte >>> (i + 1)) & 1;
            int secondBit = (brokeByte >>> i) & 1;
            if (firstBit != secondBit) {
                if (!isFirstRight) {
                    rightBit = rightBit << (i + 1);
                    break;
                } else {
                    rightBit = rightBit << i;
                    break;
                }
            }
        }
        return rightBit;
    }

    private boolean whereRightBit(int brokeBit) {
        int checker = brokeBit & 1;
        int firstBit = (brokeBit >>> 7) & 1;
        int secondBit = (brokeBit >>> 5) & 1;
        int thirdBit = (brokeBit >>> 3) & 1;
        int XORBit = firstBit ^ secondBit ^ thirdBit;

        return checker == XORBit;
    }
}
