package correcter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Destructor {
    Random random = new Random();

    public void send(String fileFrom, String fileTo) {
        try (FileInputStream fis = new FileInputStream(fileFrom);
             FileOutputStream fos = new FileOutputStream(fileTo)) {
            while (true) {
                int byteFromInput = fis.read();
                if (byteFromInput == -1) {
                    break;
                }
                int shiftRandom = 1 << random.nextInt(8);
                if ((byteFromInput & shiftRandom) > 0) {
                    byteFromInput -= shiftRandom;
                } else {
                    byteFromInput += shiftRandom;
                }
                fos.write(byteFromInput);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO Exception");
        }
    }
}
