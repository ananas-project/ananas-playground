package ananas.webapp.point_abc_server;

import java.util.Hashtable;
import java.util.Map;

public class Core {

	private static Core s_inst;

	public static Core getDefault() {
		if (s_inst == null) {
			s_inst = new Core();
		}
		return s_inst;
	}

	private final Map<String, Team> mTeams = new Hashtable<String, Team>();

	private Core() {
	}

	public Team getTeam(String name) {
		name = name + "";
		return this.mTeams.get(name);
	}

	public Team openTeam(String name) {
		name = name + "";
		Team team = this.mTeams.get(name);
		if (team == null) {
			team = new Team(name);
			this.mTeams.put(name, team);
		}
		return team;
	}

}
