//Yoad_Ashuri_311162606

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {

	/**
	 * Treat the file as an array of (unsigned) 8-bit values and sort them in-place using a bubble-sort algorithm.
	 * You may not read the whole file into memory!
	 * @param file
	 */
	public static void sortBytes(RandomAccessFile file) throws IOException {
		long fLen = file.length();
		file.seek(0);
		for(long i = 0; i < fLen; i++){
			file.seek(0);
			for(long j = 0; j < fLen - i -1; j++){
				file.seek(j);
				int left = file.read();
				int right = file.read();

				//swip left and right
				if(left>right){
					file.seek(j);
					file.write(right);
					file.write(left);
				}
			}
		}
	}


	/**
	 * Treat the file as an array of unsigned 24-bit values (stored MSB first) and sort them in-place using a bubble-sort algorithm.
	 * You may not read the whole file into memory!
	 * @param file
	 * @throws IOException
	 */
	public static void sortTriBytes(RandomAccessFile file) throws IOException {
		file.seek(0);
		for(long i = 0; i < file.length(); i += 3){               //jump in 3
			file.seek(0);                                    //reset pos
			for(long j = 0; j < file.length() - i -4 ; j += 3){   //jump in 3
				file.seek(j);
				int leftByte = read3InARow(file);
				int rightByte = read3InARow(file);
				if(leftByte > rightByte){                  //if right swip
					file.seek(j);
					Write3InARow(file,rightByte);
					Write3InARow(file,leftByte);
				}
			}
		}
	}

	/**
	 * Reads three bytes in a row from the give index in a file and returns the merged 24-bits int.
	 * @param file
	 * @throws IOException
	 * @return finalByte (24-bits integer)
	 */

	public static int read3InARow(RandomAccessFile file) throws IOException{
		int byte1 = file.read();
		int byte2 = file.read();
		int byte3 = file.read();
		//posses the less significant bytes in thier dedicated spots in the int
		byte2 <<= 8;
		byte1 <<= 16;
		//merge the bytes with the OR operator, where all bytes possesed correctly
		int ans = byte1 | byte2 | byte3;

		return ans;
	}

	/**
	 * Writes three bytes in a row from the give index in a file.
	 * @param file
	 * @throws IOException
	 */

	public static void Write3InARow(RandomAccessFile file, int given24Bits) throws IOException{
		//Seperating the 3 bytes from the given 24-bits int
		byte b3 = (byte) given24Bits;
		given24Bits >>= 8;
		byte b2 = (byte) given24Bits;
		given24Bits >>= 8;
		byte b1 = (byte) given24Bits;

		file.write(b1);
		file.write(b2);
		file.write(b3);
	}
}
