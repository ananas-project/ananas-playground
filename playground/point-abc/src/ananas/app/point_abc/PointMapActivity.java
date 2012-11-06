package ananas.app.point_abc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;

public class PointMapActivity extends MapActivity {

	private BMapManager mBMapMan;
	private MapView mMapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		{

			this.mBMapMan = new BMapManager(getApplication());
			mBMapMan.init("AEDDDBD3C213A61F6DB746900CA1C2EBE94F85D9", null);
			super.initMapActivity(mBMapMan);

			this.mMapView = (MapView) findViewById(R.id.bmapsView);
			mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件

			MapController mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
			GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
					(int) (116.404 * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 *
											// 1E6)
			mMapController.setCenter(point); // 设置地图中心点
			mMapController.setZoom(12); // 设置地图zoom级别

		}

		{

			Drawable marker = getResources()
					.getDrawable(R.drawable.ic_launcher); // 得到需要标在地图上的资源
			mMapView.getOverlays().add(new OverItemT(marker, this)); // 添加ItemizedOverlay实例到mMapView
		}

		this._installOnClickListener(R.id.button_menu);

	}

	private void _installOnClickListener(int id) {
		this.findViewById(id).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_onClick(view.getId());
			}
		});
	}

	private void _onClick(int id) {
		if (id == R.id.button_menu) {
			this.openOptionsMenu();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_set_distance: {
			Intent intent = new Intent(this, PointDistanceActivity.class);
			this.startActivity(intent);
			return true;
		}
		case R.id.menu_set_mypos: {
			// Intent intent = new Intent(this, PointTeamActivity.class);
			// this.startActivity(intent);
			return true;
		}
		case R.id.menu_set_team: {
			Intent intent = new Intent(this, PointTeamActivity.class);
			this.startActivity(intent);
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

}
