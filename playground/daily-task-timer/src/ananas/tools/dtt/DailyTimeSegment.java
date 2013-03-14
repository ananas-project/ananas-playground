package ananas.tools.dtt;

public interface DailyTimeSegment {

	long getBeginTime();

	long getEndTime();

	long getTimeSpan();

	long getTimeSpan(boolean enableNagtive);

}
