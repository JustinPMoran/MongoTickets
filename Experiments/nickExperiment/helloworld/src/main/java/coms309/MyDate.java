package coms309;

public class MyDate {
    private String month;
    private int day;
    private String year;

    public MyDate(String month, int day, String year){
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public void convertFormat(){
        switch (month) {
            case "january" -> month = "1";
            case "february" -> month = "2";
            case "march" -> month = "3";
            case "april" -> month = "4";
            case "may" -> month = "5";
            case "june" -> month = "6";
            case "july" -> month = "7";
            case "august" -> month = "8";
            case "september" -> month = "9";
            case "october" -> month = "10";
            case "november" -> month = "11";
            case "december" -> month = "12";
            default -> throw new IllegalArgumentException("Invalid month");
        }
    }

    @Override
    public String toString() {
        return "The date is: " + month + "/"
                + day + "/"
                + year ;
    }

}
