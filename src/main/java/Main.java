import Exceptions.ProgramException;
import Service.JsonInput;
import Service.JsonOutput;
import domain.DAO;
import domain.search.SearchOperation;
import domain.stat.StatOperation;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {




        String  filePath=null;
        boolean isException=false;





        try {
             filePath = new File(".").getCanonicalPath();
        } catch (IOException e) {
            isException=true;
            e.printStackTrace();

        }
        JsonOutput jsonOutput = new JsonOutput(filePath+"/JsonFiles/"+args[2]);
        JsonInput jsonInput = new JsonInput(filePath+"/JsonFiles/"+args[1]);

        DAO dao = new DAO();
        dao.setFilePath(filePath +"/persistence.properties");


        try {
            dao.initialize();
            if (args[0].equals("search")) {
                SearchOperation searchOperation = jsonInput.readSearch();
                dao.search(searchOperation);
                jsonOutput.write(searchOperation);

            } else if (args[0].equals("stat")) {
                StatOperation statOperation = jsonInput.readStat();
                dao.stat(statOperation);
                jsonOutput.write(statOperation);
            } else {
                jsonOutput.errorWrite("Некорректные данные в командной строке");
            }


    }catch (ProgramException e){
            isException=true;
            jsonOutput.errorWrite(e.getMessage());

        }

        if(isException==false) {
            System.out.println("/ / / / / / / / / / / / / / / / / / / / / /");
            System.out.println("_____________Program____Succeed____________");
            System.out.println("/ / / / / / / / / / / / / / / / / / / / / /");
        }else {
            System.out.println("/ / / / / / / / / / / / / / / / / / / / / /");
            System.out.println("_____________Something____Wrong____________");
            System.out.println("/ / / / / / / / / / / / / / / / / / / / / /");
        }



    }
}
