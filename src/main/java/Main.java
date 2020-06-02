import Exceptions.ProgramException;
import Service.JsonInput;
import Service.JsonOutput;
import Service.PersistencePropertiesSetter;
import domain.DAO;
import domain.search.SearchOperation;
import domain.stat.StatOperation;

public class Main {

    public static void main(String[] args) {
        String[] a={"search","E:/JavaJuniorTest/JsonFiles/inputSearch1.json","E:/JavaJuniorTest/JsonFiles/newOutput.json"};

        args=a;
        JsonOutput jsonOutput=new JsonOutput(args[2]);

        try {
        if (args[0].equals("set")){
            PersistencePropertiesSetter persistencePropertiesSetter=new PersistencePropertiesSetter();
            persistencePropertiesSetter.setDataBaseProperties(args);



        }else{

            JsonInput jsonInput = new JsonInput(args[1]);

            DAO dao=new DAO();
            dao.initialize();

                if(args[0].equals("search")){
                    SearchOperation searchOperation=jsonInput.readSearch();
                    dao.search(searchOperation);
                    jsonOutput.write(searchOperation);

                }else if(args[0].equals("stat")) {
                    StatOperation statOperation=jsonInput.readStat();
                    dao.stat(statOperation);
                    jsonOutput.write(statOperation);
                }else {
                    jsonOutput.errorWrite("Некорректные данные в командной строке");
                }
        }
        }catch (ProgramException e){
            jsonOutput.errorWrite(e.getMessage());
        }


    }
}
