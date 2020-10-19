
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {


        System.out.print("getQuoted test status :" + (getQuotedTest() == true ? "passed" : "failed") + "\n");
        System.out.print("readUntil test status :" + (readUntilTest() == true ? "passed" : "failed") + "\n");
        System.out.print("filterOut test status :" + (filterOutTest() == true ? "passed" : "failed") + "\n");
        System.out.print("readNumber test status :" + (readNumberTest() == true ? "passed" : "failed") + "\n");
        System.out.print("sortBytes test status :" + (sortBytesTest() == true ? "passed" : "failed") + "\n");
        System.out.print("sortTriBytes test status :" + (sortTriBytesTest() == true ? "passed" : "failed") + "\n");
        try{
            System.out.print("InMemoryDictionary test status :" + (InMemoryDictionaryTest() == true ? "passed" : "failed") + "\n");
        }catch (IOException e){
            System.out.print("InMemoryDictionary test status : false");
        }

    }

    private static boolean getQuotedTest() {

        byte[] data = "\"123456789123456789\"".getBytes();
        byte[] data2 = "\"123456789123456789".getBytes();
        byte[] data3 = "\"1234567\"89123456789".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        InputStream input2 = new ByteArrayInputStream(data2);
        InputStream input3 = new ByteArrayInputStream(data3);

        List<InputStream> ListOfInputsToTest = new ArrayList<>();
        ListOfInputsToTest.add(input);
        ListOfInputsToTest.add(input2);
        ListOfInputsToTest.add(input3);

        int i = 1;
        for (InputStream in : ListOfInputsToTest) {
            try {
                List<Byte> outPut = Streams.getQuoted(in);
                if(!(outPut.size() == (data.length - 2) || outPut.size() == (data2.length - 1) || outPut.size() == 7)) return false;
                i++;
            } catch (IOException e) {
                System.out.print("getQuoted Got an IO Exception");
                return false;
            }
        }
        return true;
    }

    private static boolean readUntilTest() {

        String endMark = "789";
        String endMark2 = "67";
        String endMark3 = "568";
        String[] endMarks = new String[3];
        endMarks[0] = endMark;
        endMarks[1] = endMark2;
        endMarks[2] = endMark3;

        String[] result = new String[3];
        result[0] = "123456";
        result[1] = "12345";
        result[2] = "123456789";

        byte[] data = "123456789".getBytes();
        Reader reader = new InputStreamReader(new ByteArrayInputStream(data));
        Reader reader2 = new InputStreamReader(new ByteArrayInputStream(data));
        Reader reader3 = new InputStreamReader(new ByteArrayInputStream(data));

        List<Reader> ListOfReadersToTest = new ArrayList<>();
        ListOfReadersToTest.add(reader);
        ListOfReadersToTest.add(reader2);
        ListOfReadersToTest.add(reader3);

        int i = 0;
        for (Reader in : ListOfReadersToTest) {
            try {
                String outPut = Streams.readUntil(in, endMarks[i]);
                if(!(outPut.equals(result[i]))) return false;
                i++;
            } catch (IOException e) {
                System.out.print("readUntil Got an IO Exception");
                return false;
            }

        }
        return true;
    }

    private static boolean filterOutTest() {
        String path = "src/test4.txt";

        try {
            byte[] data = "\"123456789123456789\"".getBytes();
            InputStream input = new ByteArrayInputStream(data);
            File file = new File(path);
            try {
                OutputStream output = new FileOutputStream(file);
                byte badByte = '1';
                try {
                    Streams.filterOut(input, output, badByte);
                    output.flush();
                    if (!(file.length() == data.length - 2)) return false;
                } catch (IOException e) {
                    return false;
                }
            } catch (FileNotFoundException e) {
                return false;
            }

            return true;
        }
        finally {
            deleteFileContent(path);

        }
    }

    private static boolean readNumberTest() {

        byte[] data = {0x12, 0x34, 0x56, 0x78, 0x0a};
        InputStream input = new ByteArrayInputStream(data);

        List<InputStream> ListOfInputsToTest = new ArrayList<>();
        ListOfInputsToTest.add(input);


        long[] expectedNumbers = new long[1];
        expectedNumbers[0] = 78187493386l;

        int i = 0;
        for (InputStream in : ListOfInputsToTest) {
            try {
                long resultNumber = Streams.readNumber(in);
                if(!(resultNumber == expectedNumbers[i])) return false;
                i++;
            } catch (IOException e) {
                System.out.print("readNumber Got an IO Exception");
                return false;
            }
        }
        return true;
    }

    private static boolean sortBytesTest() {

        String data = "124365987";
        String result = "123456789";
        String path = "src/test2.txt";

        try{
            try {
                writeDataToFile(path, data, 0, data.length());
            }catch (IOException e) { }

            try {

                //Origin Data
                RandomAccessFile file = new RandomAccessFile(path, "rw");
                byte[] updatedFile = readDataFromFile(path, 0, (int) file.length());
                Reader reader = new InputStreamReader(new ByteArrayInputStream(updatedFile));
                String originFile = Streams.readUntil(reader, "\"");

                //Test the insert data
                if(!(originFile.equals(data))) return false;

                //run the method
                RandomAccess.sortBytes(file);

                //Result Data
                updatedFile = readDataFromFile(path, 0, (int) file.length());
                reader = new InputStreamReader(new ByteArrayInputStream(updatedFile));
                String resultFile = Streams.readUntil(reader, "\"");

                //Test the sortBytes method
                if(!(resultFile.equals(result))) return false;

            } catch (IOException e) {
            }

            return true;
        }finally {
            deleteFileContent(path);
        }

    }
    private static boolean sortTriBytesTest() {

        String data = "200100300500120";
        String result = "100120200300500";
        String path = "src/test3.txt";
        try{
            try {
                writeDataToFile(path, data, 0, data.length());
            }catch (IOException e) { }

            try {

                //Origin Data
                RandomAccessFile file = new RandomAccessFile(path, "rw");
                byte[] updatedFile = readDataFromFile(path, 0, (int) file.length());
                Reader reader = new InputStreamReader(new ByteArrayInputStream(updatedFile));
                String originFile = Streams.readUntil(reader, "\"");

                //Test the insert data
                if(!(originFile.equals(data))) return false;

                //run the method
                RandomAccess.sortTriBytes(file);

                //Result Data
                updatedFile = readDataFromFile(path, 0, (int) file.length());
                reader = new InputStreamReader(new ByteArrayInputStream(updatedFile));
                String resultFile = Streams.readUntil(reader, "\"");

                //Test the sortBytes method
                if(!(resultFile.equals(result))) return false;

            } catch (IOException e) {
            }
            return true;
        }finally {
            deleteFileContent(path);
        }

    }

    private static boolean InMemoryDictionaryTest() throws IOException {
        String path = "src/test1.txt";
        try{
            File file = new File(path);
            InMemoryDictionary inMemoryDictionary = new InMemoryDictionary(file);

            //enter 2 key:value
            inMemoryDictionary.open();
            inMemoryDictionary.put("goodKey1", "goodValue1");
            inMemoryDictionary.put("goodKey2", "goodValue2");
            Set<String> keys = inMemoryDictionary.keySet();
            if(!(keys.size() == 2)) return false;         //check 2 keys
            if(!(file.length() == 0)) return false;       //check file still empty

            inMemoryDictionary.close();
            if(!(file.length() == 40)) return false;      //check file filled with the 2 inputs
            if(!(keys.size() == 0)) return false;         //check 0 keys after closing

            inMemoryDictionary.open();
            keys = inMemoryDictionary.keySet();
            inMemoryDictionary.put("goodKey3", "goodValue3");
            if(!(file.length() == 40)) return false;      //check file didnt changed
            if(!(keys.size() == 3)) return false;         //check 3 keys

            inMemoryDictionary.close();
            if(!(file.length() == 60)) return false;      //check file didnt changed
            if(!(keys.size() == 0)) return false;         //check 0 keys after closing

            return true;
        }finally {
            deleteFileContent(path);
        }
    }


    private static byte[] readDataFromFile(String file, int pos, int size) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(pos);
        byte[] bytesToRead = new byte[size];
        raf.read(bytesToRead);
        raf.close();
        return bytesToRead;
    }

    private static void writeDataToFile(String file, String data, int pos, int size) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(pos);
        raf.write(data.getBytes());
        raf.close();
    }

    private static void deleteFileContent(String filePath)  {
       File file = new File(filePath);
       try {
           PrintWriter writer = new PrintWriter(file);
           writer.print("");
           writer.close();
       }catch (FileNotFoundException e){

       }

    }

}
