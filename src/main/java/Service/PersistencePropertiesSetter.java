package Service;

import Exceptions.ProgramException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class PersistencePropertiesSetter {
    public void setDataBaseProperties(String[] args) throws ProgramException {
        String filePath = "/resources/persistence.properties";
        Properties prop = new Properties();
        if (args.length!=4){
            throw new ProgramException(" Неверное число параметров ");
        }

        try (Writer inputStream = new FileWriter(filePath)) {

            prop.setProperty("url", args[1]);
            prop.setProperty("username", args[2]);
            prop.setProperty("password", args[3]);

            prop.store(inputStream, "Database information");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ProgramException(" Не удается прочитать файл");
        }


    }
}
