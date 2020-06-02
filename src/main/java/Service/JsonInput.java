package Service;


import Exceptions.ProgramException;
import domain.search.SearchOperation;
import domain.stat.StatOperation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

// Этот класс считаевает данные из json файла в JSONObject, который затем
// передается в конструктор SearchOperation или StatOperation

// В конструкторах JSONObject разбивается на параметры,
// из которых затем формируется запрос в базу данных


public class JsonInput {
    private String inputPath;

    private JSONParser parser = new JSONParser();

    public StatOperation readStat() throws ProgramException {

        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "windows-1251"))) {

            JSONObject jsonObject = (JSONObject) parser.parse(input);
            StatOperation statOperation = new StatOperation(jsonObject);

            return statOperation;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramException("Не найден файл");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ProgramException("Некорректный json файл");
        }
    }




    public SearchOperation readSearch() throws ProgramException {

        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "windows-1251"))) {

            JSONObject jsonObject = (JSONObject) parser.parse(input);

            JSONArray array = (JSONArray) jsonObject.get("criterias");
            SearchOperation searchOperation = new SearchOperation(array);

            return searchOperation;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramException("Не найден файл");

        } catch (ParseException e) {
            e.printStackTrace();
            throw new ProgramException(" Некорректные данные в input ");

        }

    }
    public JsonInput(String inputPath ){
        this.inputPath=inputPath;
    }
}
