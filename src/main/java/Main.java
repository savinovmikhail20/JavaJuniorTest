import Exceptions.ProgramException;
import Service.JsonInput;
import Service.JsonOutput;
import domain.search.SearchOperation;

public class Main {

    public static void main(String[] args) {

        JsonInput jsonInput = new JsonInput(args[1]);
        JsonOutput jsonOutput=new JsonOutput(args[2]);

        if(args[0].equals("search")){
            try {

                SearchOperation searchOperation=jsonInput.readSearch();

            } catch (ProgramException e) {
                jsonOutput.errorWrite(e.getMessage(),args[2]);
            }

        }

    }
}
