package bot;
/**
 * Created by mmx on 07.02.2018.
 */

import login.ConnExec;

import java.util.Arrays;

/**
 * read-only class to store some data about auto-s
 */
public  class KmzData {
    //private final ArrayList<String> types = new ArrayList<String>();
    //private final Map<String,String> types = new ArrayList<String>();
//    private final String[] types = {"Самосвал","Шасси", "Грузовик", "Тягач"};


    private ConnExec connExec;

    private final String[] types = {"Dumper","Chassis", "Truck", "Tractor"};

//    private final String[][] model = {{"Самосвал","Самосвал","Самосвал","Шасси","Грузовик","Грузовик","Грузовик","Грузовик","Тягач","Тягач","Тягач"},
private final String[][] model = {{"Dumper","Dumper","Dumper","Chassis","Truck","Truck","Truck","Truck","Tractor","Tractor","Tractor"},
            {"65115","6520","65201","4308" ,"65117", "65207", "6560", "65806" ,
                    "5490", "6460", "65206"},
        {"1","1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1"}};

//    {"sam_65115","sam_6520", "sam_65201", "shassi_4308", "gr_65117", "gr_65207", "gr_6560", "gr_65806",
//            "t_5490", "t_6460", "t_65206"}};

    public KmzData (){

    }
//    public KmzData (ConnExec connExec){
//        this.connExec = connExec;
//    }

    public String[] getTypes() {
        return types;
    }


    public String[] getModelList() {
        return model[1];
    }

    public String[] getModelList(String oneTypeOnly) {
        String [] modelList = new String[this.getModelList().length];
        int counter = 0;
        for(int i=0; i<model[0].length; i++){
            if(oneTypeOnly.equals(model[0][i])) {
                modelList[counter] = model[1][i];
                counter++;
            }
        }

        String[] retArray = new String[modelList.length];
        retArray = Arrays.copyOf (modelList, counter);;


        return retArray;
    }

    public String[][] getModel() {
        return model;
    }

    public boolean isTypeParametr(String param){
        boolean result = false;
        for(String type : getTypes()){
            if (param.equals(type)){
                result = true;
            }
        }
        return result;
    }

    public boolean isModelParametr(String param){
        boolean result = false;
        for(String model : getModelList()){
            if (param.equals(model)){
                result = true;
            }
        }
        return result;
    }

    public String getPicAddrByModel(String modelName){
        String strToReturn = "not found";
        for(int i=0; i<model[1].length; i++){
            if(modelName.equals(model[1][i])) {
                strToReturn = model[2][i];
            }
        }
        return strToReturn;
    }

}
