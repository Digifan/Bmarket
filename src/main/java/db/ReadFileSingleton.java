package db;

import java.io.File;
import java.util.Objects;

public class ReadFileSingleton {
    private static ReadFileSingleton instance;

    //private static final Logger logger = Logger.getLogger(ReadFileSingleton.class.getName());

    private ReadFileSingleton() {
    }

    public static synchronized ReadFileSingleton getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ReadFileSingleton();
        }
        return instance;
    }

    public File getFileSingleton(String fileName) {
        File file = new File(fileName);

        return file;
    }

    /*private List<String> readFile(Path filePath, List<String> fileSource) {
        try (Stream<String> stringStream = Files.lines(filePath)){
            fileSource = stringStream.toList();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Read file error");
        }
        return fileSource;
}*/

}
