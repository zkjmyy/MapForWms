package com.gxsn.gaodemapdemo.google;


import com.google.android.gms.maps.model.UrlTileProvider;
import com.gxsn.gaodemapdemo.gaode.PositionModel;
import com.gxsn.gaodemapdemo.gaode.PositionUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zkj on 2017/08/02
 */

public class GoogleHeritageScopeTileProvider extends UrlTileProvider {

    private String mRootUrl;
    //默认瓦片大小
    private static int titleSize = 256;//a=6378137±2（m）
    //基本参数
    private final double initialResolution= 156543.03392804062;//2*Math.PI*6378137/titleSize;
    private final double originShift      = 20037508.342789244;//2*Math.PI*6378137/2.0; 周长的一半

    private final double HALF_PI = Math.PI / 2.0;
    private final double RAD_PER_DEGREE = Math.PI / 180.0;
    private final double HALF_RAD_PER_DEGREE = Math.PI / 360.0;
    private final double METER_PER_DEGREE = originShift / 180.0;//一度多少米
    private final double DEGREE_PER_METER = 180.0 / originShift;//一米多少度


    public GoogleHeritageScopeTileProvider() {
        super(titleSize, titleSize);
        mRootUrl = "http://xxxxx你自己要加载/wms?LAYERS=cwh:protect_region_38_20160830&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&SRS=EPSG%3A900913&BBOX=";
    }


    public GoogleHeritageScopeTileProvider(int i, int i1) {
        super(i, i1);
    }

    @Override
    public URL getTileUrl(int x, int y, int level) {

        try {
            String url = mRootUrl + TitleBounds(x, y, level);
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据像素、等级算出坐标
     *
     * @param p
     * @param zoom
     * @return
     */
    private double Pixels2Meters(int p, int zoom) {
        return p * Resolution(zoom) - originShift;
    }

    /**
     * 根据瓦片的x/y等级返回瓦片范围
     *
     * @param tx
     * @param ty
     * @param zoom
     * @return
     */
    private String TitleBounds(int tx, int ty, int zoom) {
        double minX = Pixels2Meters(tx * titleSize, zoom);
        double maxY = -Pixels2Meters(ty * titleSize, zoom);
        double maxX = Pixels2Meters((tx + 1) * titleSize, zoom);
        double minY = -Pixels2Meters((ty + 1) * titleSize, zoom);

        //转换成经纬度
        minX=Meters2Lon(minX);
        minY=Meters2Lat(minY);
        maxX=Meters2Lon(maxX);
        maxY=Meters2Lat(maxY);
        PositionModel position1 = PositionUtil.gcj_To_Gps84(minY,minX);
        minX = position1.getWgLon();
        minY = position1.getWgLat();
        PositionModel position2 = PositionUtil.gcj_To_Gps84(maxY,maxX);
        maxX = position2.getWgLon();
        maxY = position2.getWgLat();

        minX=Lon2Meter(minX);
        minY=Lat2Meter(minY);
        maxX=Lon2Meter(maxX);
        maxY=Lat2Meter(maxY);

        return minX + "," + Double.toString(minY) + "," + Double.toString(maxX) + "," + Double.toString(maxY) + "&WIDTH=256&HEIGHT=256";
    }

    /**
     * 计算分辨率
     *
     * @param zoom
     * @return
     */
    private double Resolution(int zoom) {
        return initialResolution / (Math.pow(2, zoom));
    }

    /**
     * X米转经纬度
     */
    private double Meters2Lon(double mx) {
        double lon = mx * DEGREE_PER_METER;
        return lon;
    }

    /**
     * Y米转经纬度
     */
    private double Meters2Lat(double my) {
        double lat = my * DEGREE_PER_METER;
        lat = 180.0 / Math.PI * (2 * Math.atan(Math.exp(lat * RAD_PER_DEGREE)) - HALF_PI);
        return lat;
    }

    /**
     * X经纬度转米
     */
    private double Lon2Meter(double lon) {
        double mx = lon * METER_PER_DEGREE;
        return mx;
    }

    /**
     * Y经纬度转米
     */
    private double Lat2Meter(double lat) {
        double my = Math.log(Math.tan((90 + lat) * HALF_RAD_PER_DEGREE)) / (RAD_PER_DEGREE);
        my = my * METER_PER_DEGREE;
        return my;
    }


}
