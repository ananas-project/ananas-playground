package ananas.app.ots.v2.pojo;

import java.util.ArrayList;
import java.util.List;

public class LocationFragmentFormat extends POJO {

	private String taskId;
	private List<OTSLocation> list;

	public LocationFragmentFormat() {
		list = new ArrayList<OTSLocation>();
		taskId = "undef";
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<OTSLocation> getList() {
		return list;
	}

	public void setList(List<OTSLocation> list) {
		this.list = list;
	}

	public static void normal(LocationFragmentFormat lff) {
		// TODO Auto-generated method stub

	}

}
