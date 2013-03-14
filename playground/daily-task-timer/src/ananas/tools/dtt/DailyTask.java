package ananas.tools.dtt;

import java.util.Collection;

public interface DailyTask extends DailyRecorder {

	long getTimeQuota();

	long getTimeSpan();

	long getTimeSpan(boolean noIncludeActive);

	String getName();

	Collection<DailyTimeSegment> getSegments();

}
