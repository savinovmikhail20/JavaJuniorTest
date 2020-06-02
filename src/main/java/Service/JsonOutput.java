package Service;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


// Этот класс выводит данные в json.output

public class JsonOutput {
    private String outputPath;

    public  void errorWrite( String message, String outputPath){
        try ( FileWriter writer= new FileWriter(outputPath) ) {

            JSONObject jo = new JSONObject();
            jo.put("type", "error");
            jo.put("message", message);

            writer.write(jo.toString());

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }











    }

    public JsonOutput(String outputPath ){
        this.outputPath=outputPath;
    }
}
