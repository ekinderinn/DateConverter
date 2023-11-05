package org.example;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class DateConverter {
    private static String format1="dd/MM/yyyy/EEEE";
    private static String format2="dd/M/yyyy/EEEE";
    private static String format3="yyyy-MM-dd EEEE";
    private static String format4="EEEE dd.MM.yyyy";

    private List<String> formats = new ArrayList<String>();

    private List<String> datesFromFile = new ArrayList<String>();
    private Set<MyDate> convertedDates = new HashSet<MyDate>();

    public void initFormats(){
        formats.add(format1);
        formats.add(format2);
        formats.add(format3);
        formats.add(format4);
    }
    public void readDates(){
        try {
            File inputFile = new File("src/main/java/org/example/InputData.txt");
            Scanner myReader = new Scanner(inputFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                datesFromFile.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }
    public void convertDate() {
        initFormats();
        readDates();
        for(String d: datesFromFile){
           for(int j =0; j < 4; j++){
               try {
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formats.get(j), Locale.ENGLISH);
                   LocalDate date = LocalDate.parse(d,formatter);
                   convertedDates.add( new MyDate(date.getDayOfMonth(),date.getMonthValue(),date.getYear(),date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)));
                   break;
               }
               catch (DateTimeException e){
                  // System.out.println("Date " + d + " is in wrong form!");
               }
           }
       }
       writeToFile();

    }
    private List<MyDate> sortDates() {
        List<MyDate> dateList = new ArrayList<>(convertedDates);

        dateList.sort(Comparator.comparing(MyDate::getYear)
                .thenComparing(MyDate::getMonth)
                .thenComparing(MyDate::getDay));

        return dateList;
    }
    public void writeToFile(){
        try {
            FileWriter myWriter = new FileWriter("src/main/java/org/example/MyData.txt");
            PrintWriter printWriter = new PrintWriter(myWriter);
            for(MyDate d: sortDates()){
                printWriter.printf("day = %d, month = %d, year = %d, weekday = %s\n", d.getDay(),d.getMonth(),d.getYear(),d.getWeekday());
            }
            printWriter.close();
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
