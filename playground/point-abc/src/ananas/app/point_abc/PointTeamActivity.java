package ananas.app.point_abc;

import java.util.ArrayList;
import java.util.List;

import ananas.app.point_abc.core.Core;
import ananas.app.point_abc.core.DefaultPointABCClientListener;
import ananas.app.point_abc.core.DefaultThreadProvider;
import ananas.app.point_abc.core.IPointABCClient;
import ananas.app.point_abc.core.Team;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PointTeamActivity extends Activity {

	private Core mCore;
	private EditText mEditUserId;
	private EditText mEditTeamId;

	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);

		this.mCore = Core.getDefault();

		this.mEditUserId = (EditText) this.findViewById(R.id.edit_user_id);
		this.mEditTeamId = (EditText) this.findViewById(R.id.edit_team_id);

		// list view
		this.mListView = new ListView(this);
		LinearLayout p4list = (LinearLayout) this
				.findViewById(R.id.pane_for_list);
		p4list.addView(this.mListView);
		this._reloadDataForMemberList();

		// install listener
		this._installOnClickListener(R.id.button_back);
		this._installOnClickListener(R.id.button_refresh);

	}

	private void _reloadDataForMemberList() {
		try {
			Team team = this.mCore.getClient().getTeam();
			String[] users = team.listUsers();
			List<String> data = new ArrayList<String>();
			for (String user : users) {
				data.add(user);
			}
			this.mListView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1, data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _installOnClickListener(int id) {

		this.findViewById(id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				_onClick(arg0.getId());
			}
		});
	}

	private void _onClick(int id) {
		switch (id) {
		case R.id.button_refresh:
			this.doRefresh();
			break;
		case R.id.button_back:
			this.doBack();
			break;
		default:
		}
	}

	private void _valueToUI() {
		this.mEditTeamId.setText(this.mCore.getClient().getTeamId());
		this.mEditUserId.setText(this.mCore.getClient().getUserId());
	}

	private void _uiToValue() {
		String tid = this.mEditTeamId.getText().toString().trim();
		String uid = this.mEditUserId.getText().toString().trim();
		this.mCore.getClient().setTeamId(tid);
		this.mCore.getClient().setUserId(uid);
	}

	private void doBack() {
		this._uiToValue();

		DefaultPointABCClientListener l = new DefaultPointABCClientListener(
				this);
		DefaultThreadProvider tp = new DefaultThreadProvider(this);
		this.mCore.getClient().push(l, tp);

		Intent intent = new Intent(this, PointMapActivity.class);
		this.startActivity(intent);
	}

	private void doRefresh() {
		this._uiToValue();

		DefaultPointABCClientListener l = new DefaultPointABCClientListener(
				this) {

			@Override
			public void onEvent(IPointABCClient client, int code, String message) {
				super.onEvent(client, code, message);
				PointTeamActivity.this._reloadDataForMemberList();
			}

		};
		DefaultThreadProvider tp = new DefaultThreadProvider(this);
		this.mCore.getClient().pull(l, tp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		this.mCore.save();
		super.onPause();
	}

	@Override
	protected void onResume() {
		this.mCore.load();
		this._valueToUI();
		super.onResume();
	}

}
