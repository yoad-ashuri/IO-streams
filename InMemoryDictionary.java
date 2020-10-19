//Yoad_Ashuri_311162606

import java.io.*;
import java.util.TreeMap;


/**
 * Implements a persistent dictionary that can be held entirely in memory.
 * When flushed, it writes the entire dictionary back to a file.
 *
 * The file format has one keyword per line:
 * word:def1
 * word2:def2
 * ...
 *
 * Note that an empty definition list is allowed (in which case the entry would have the form: word:)
 *
 */
public class InMemoryDictionary extends TreeMap<String,String> implements PersistentDictionary  {
	private static final long serialVersionUID = 1L; // (because we're extending a serializable class)
	private RandomAccessFile classFile; // Holds the used file

	public InMemoryDictionary(File dictFile) throws IOException{
		this.classFile = new RandomAccessFile(dictFile, "rw");
	}

	@Override
	public void open() throws IOException {
		classFile.seek(0);
		int colIndex;
		String line,word,def;

		line = classFile.readLine();
		while(line != null) {
			colIndex = line.indexOf(':');               //find the":" index
			word = line.substring(0, colIndex);         //separate the word from line
			def = line.substring(colIndex + 1);         //separate the definition from line
			this.put(word, def);
			line = classFile.readLine();                //read next line
		}
	}

	@Override
	public void close() throws IOException {
		try {
			//erase file's previous content
			classFile.setLength(0);

			String def;
			//create a line with the strings stored in the TreeMap
			for(String val : this.keySet()){
				def = this.get(val);
				classFile.writeBytes(val + ":" + def + "\n");         //create the line itself with ":" that separate the word and def
			};
			classFile.setLength(classFile.length()-1);
		}
		// no meter what, close the file.
		finally{
			classFile.close();
		}
	}

}
