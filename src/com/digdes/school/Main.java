package com.digdes.school;


import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args){
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName'>'Федоров', 'active'=false, 'id'=3, 'age'=40");
            //Изменение значения которое выше записывали
            List<Map<String,Object>> result2 = starter.execute("UPDATE VALUES 'active'=true, 'cost'=10.1 WHERE 'id'=3");
            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            List<Map<String,Object>> result3 = starter.execute("SELECT");

            //Select check
            print(result3);

            starter.execute("DELETE VALUES WHERE 'id'=3");
            var result4 = starter.execute("SELECT");
            //Delete check
            print(result4);


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void print(List<Map<String, Object>> result3) {
        if(result3.size() != 0){
            result3.forEach(map -> {
                map.forEach((key, value) -> System.out.println(key + " = " + value));
                System.out.println();
            });
        }else{
            System.out.println("Empty list");
        }

    }
}

