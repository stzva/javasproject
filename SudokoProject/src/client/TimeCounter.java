package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.Timer;

/*
 * counts the time played
 * a timer increses the time variable by one each second
 * keeps and updates a jtextfield with the formatted time
 */

public class TimeCounter {

    private class TimeEventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            time++;
            timeToString.setText(getTimeString());
        }

        public String getTimeString() {
            int totalSeconds = time;
            int hours = totalSeconds / (60 * 60);
            totalSeconds = totalSeconds - hours * (60 * 60);

            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            String result = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            return result;

        }
    }
    private Timer timer;
    private JTextField timeToString;
    private int time;
    private boolean done;

    public TimeCounter(JTextField timeField) {

        timer = new Timer(1000, new TimeEventHandler());
        timeToString = timeField;
        setDone(false);

    }

    public void startCount() {
        time = 0;
        timer.start();
    }

    public void stopCount() {
        timer.stop();
        setDone(true);
    }
    
    public int getTime() {
        return time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
    
    
}
