package com.gxsn.gaodemapdemo.tianditu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gxsn.gaodemapdemo.R;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

/**
 * Created by zkj on 2017/08/11
 * GaoDeMapDemo
 */

public class TianDiTuMapActivity extends AppCompatActivity{


    private MapView mTianDiTuMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianditu);

        mTianDiTuMap = ((MapView) findViewById(R.id.mv_tianditu));

        String url = "http://t7.tianditu.com/DataServer?T=vec_c&x=13394&y=2704&l=14";
        mTianDiTuMap.setCustomTileService(url);


        mTianDiTuMap.setBuiltInZoomControls(true);

        MapController mMapController = mTianDiTuMap.getController();

        GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));

        mMapController.setCenter(point);

        mMapController.setZoom(12);

    }
}
