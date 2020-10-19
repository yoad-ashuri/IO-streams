//Yoad_Ashuri_311162606

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Streams {
	/**
	 * Read from an InputStream until a quote character (") is found, then read
	 * until another quote character is found and return the bytes in between the two quotes. 
	 * If no quote character was found return null, if only one, return the bytes from the quote to the end of the stream.
	 * @param in
	 * @return A list containing the bytes between the first occurrence of a quote character and the second.
	 */
	public static List<Byte> getQuoted(InputStream in) throws IOException {
		int c;
		List<Byte> ans = new LinkedList<Byte>();
		try {
			c = in.read();
			while ((c != -1) && (c != '\"') ) {
				c = in.read();
			}
			if (c == -1) {
				return null;
			}
			c = in.read();
			while ((c != -1) && (c != '\"') ) {
				ans.add((byte)(c-48));
				c = in.read();
			}
			return ans;

		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	/**
	 * Read from the input until a specific string is read, return the string read up to (not including) the endMark.
	 * @param in the Reader to read from
	 * @param endMark the string indicating to stop reading. 
	 * @return The string read up to (not including) the endMark (if the endMark is not found, return up to the end of the stream).
	 */
	public static String readUntil(Reader in, String endMark) throws IOException {
		int c;
		int i = 0;
		char temp;
		int endMarkLen = endMark.length();
		StringBuilder str = new StringBuilder();
		try {
			c = in.read();
			while ((c != -1)) {
				temp = (char)c;
				str.append(temp);
				if (temp == endMark.charAt(i)) {
					i++;
					if (i == endMarkLen) {
						return str.substring(0, str.length()- i);
					}
				}else {
					i = 0;
				}
				c = in.read();
			}
			return str.toString();

		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Copy bytes from input to output, ignoring all occurrences of badByte.
	 * @param in
	 * @param out
	 * @param badByte
	 */
	public static void filterOut(InputStream in, OutputStream out, byte badByte) throws IOException {

		try {
			int c = in.read();
			while (c != -1) {
				if ((byte) c != badByte) {
					out.write((byte) c);
				}
				c = in.read();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Read a 40-bit (unsigned) integer from the stream and return it. The number is represented as five bytes,
	 * with the most-significant byte first. 
	 * If the stream ends before 5 bytes are read, return -1.
	 * @param in
	 * @return the number read from the stream
	 */
	public static long readNumber(InputStream in) throws IOException {
		long ans = 0;
		int count = 0;
		try {

			int c = in.read();
			while (c != -1) {
				ans <<= 8;
				count++;
				ans |= c;
				if (count > 5) {
					return -1;
				}
				c = in.read();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return count<5 ? -1 : ans;
	}
}
