package simCity;

//import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Clock class to take care of sim city time
 * @author garv
 */
public class Clock {
	public static Clock globalClock;
	
	static Double timeOfDay = 8.0;
	static int dateOfMonth = 0;
	static int monthOfYear = 0;
	static int year = 0;
	
	Timer time = new Timer();
	final static int halfAnHour = 6000;
	
	//pointers to labels (updated in updatelabel)
	JLabel timeLabel;
	JLabel lightLabel;
	
	ImageIcon dawnLight = new ImageIcon("src/simCity/gui/images/timeOfDay/dawn.png");
	ImageIcon dayLight = new ImageIcon("src/simCity/gui/images/timeOfDay/day.png");
	ImageIcon sunsetLight = new ImageIcon("src/simCity/gui/images/timeOfDay/sunset.png");
	ImageIcon nightLight = new ImageIcon("src/simCity/gui/images/timeOfDay/night.png");
	
	//constructor
	public Clock(JLabel l, JLabel l2) {
		timeOfDay = 8.0;
		monthOfYear = 11;
		dateOfMonth = 26;
		year = 2013;
		
		//String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		keepTime();
		
		timeLabel = l;
		lightLabel = l2;
		updateLabel();
		globalClock = this;
	}
	
	public Clock() {
		timeOfDay = 8.0;
		monthOfYear = 11;
		dateOfMonth = 26;
		year = 2013;
		
		//String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		keepTime();
		
		updateLabel();
		globalClock = this;
	}
	
	
	//accessors
	public Double getTime() {
		return timeOfDay;
	}
	public int getDate() {
		return dateOfMonth;
	}
	public int getMonth() {
		return monthOfYear;
	}
	public int getYear() {
		return year;
	}

	//private methods
	private void keepTime() {
		//handle time loops
		if(timeOfDay == 24.0) {
			timeOfDay = 0.0;
			dateOfMonth++;
		}
		if(monthOfYear < 8) {
			if(monthOfYear%2 == 0) {
				if(monthOfYear == 2) {
					if(year%4 == 0) {
						if(dateOfMonth == 30) {
							dateOfMonth = 1;
							monthOfYear++;
						}
					} else {
						if(dateOfMonth == 29) {
							dateOfMonth = 1;
							monthOfYear++;
						}
					}
				} else {
					if(dateOfMonth == 31) {
						dateOfMonth = 1;
						monthOfYear++;
					}
				}
			}
			else if(monthOfYear%2 == 1) {
				if(dateOfMonth == 32) {
					dateOfMonth = 1;
					monthOfYear++;
				}
			}
		} else {
			if(monthOfYear%2 == 0) {
				if(dateOfMonth == 32) {
					dateOfMonth = 1;
					monthOfYear++;
				}
			}
			else if(monthOfYear%2 == 1) {
				if(dateOfMonth == 31) {
					dateOfMonth = 1;
					monthOfYear++;
				}
			}
		}
		if(monthOfYear == 13) {
			monthOfYear = 1;
			year++;
		}
		
		//schedule timer, update time
		time.schedule(new TimerTask() {
			public void run() {
				timeOfDay = timeOfDay + 0.5;
				//System.out.println(getTime() + ", " + getMonth() + "/" + getDate() + "/" + getYear());
				updateLabel();
				keepTime();
			}
		}, 
		halfAnHour);
	}
	
	private void updateLabel() {
		String tmp = new String();
		Double tmpTime = getTime();
		Double tmp12HrTime = tmpTime;
		int hour;
		
		//convert to 12 hr clock
		if(tmpTime >= 13.0)
			tmp12HrTime -= 12;
		
		//update hours
		hour = tmp12HrTime.intValue();
		//check 12:30am exception
		if(hour == 0 && hour != tmp12HrTime) {
			tmp += "12:";
		} else {
			//add hour
			if(hour < 10) tmp += "0";
			tmp += hour + ":";
		}
		
		//update minutes
		if(hour == tmp12HrTime)
			tmp += "00";
		else
			tmp += "30";
		
		//check am/pm
		if(tmpTime >= 12.0)			
			tmp += " PM";
		else
			tmp += " AM";

		//add date & update labels
		tmp += ", " + getMonth() + "/" + getDate() + "/" + getYear();
		timeLabel.setText(tmp);
		
		if(tmpTime >= 5.0 && tmpTime < 11.0) {
			lightLabel.setIcon(dawnLight);//dawn
		} else if(tmpTime >= 11.0 && tmpTime < 17.0) {
			lightLabel.setIcon(dayLight);//day
		} else if(tmpTime >= 17.0 && tmpTime < 20.0) {
			lightLabel.setIcon(sunsetLight);//sunset
		} else {
			lightLabel.setIcon(nightLight);//night
		}
	}
}
