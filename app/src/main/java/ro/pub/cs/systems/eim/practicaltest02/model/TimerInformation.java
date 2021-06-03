package ro.pub.cs.systems.eim.practicaltest02.model;

public class TimerInformation {

    public TimerInformation(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    private String hour;
    private String minute;
//    private String url;


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "timer{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }


}
