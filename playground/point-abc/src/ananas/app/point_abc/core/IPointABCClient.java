package ananas.app.point_abc.core;

public interface IPointABCClient {

	void push(IPointABCClientListener l, IThreadProvider tp);

	void pull(IPointABCClientListener l, IThreadProvider tp);

	void setDistance(String name, int dist);

	void setUserId(String uid);

	void setTeamId(String tid);

	String getUserId();

	String getTeamId();

	Team getTeam();

}
