package com.creativeapps.salat_times.MuslimCompanion;



import java.util.Calendar;

/**
 * Created by Sifat Ullah on 7/11/2018.
 */

public class HanafiAsr {

    private Calendar calendar= Calendar.getInstance();
    private double julianDay = DateUtils.dateToJulian(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));

    private double time = (julianDay-2451545)/36525;

    private double asrTime=2.0;
    private double asrDelay= 0.0;

    private double latitude;
    private double longitude;
    private int rule;


    public HanafiAsr(Calendar calendar, double latitude, double longitude) {
        this.calendar = calendar;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HanafiAsr(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;


    }

    public long getAsrTimestamp(){

        double hoursInTheDay = getDhuhr() + getTimeShadowSizeDifference(asrTime) + asrDelay;
        long realTime = DateUtils.utcTimeToTimestamp(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hoursInTheDay);
        return realTime - (realTime%60000L) + 60000L;
    }

    public double getDhuhr(){
        return 12.0 - (longitude/15.0) - (equationOfTime(time)/60.0);
    }
    public double getTimeShadowSizeDifference(double times) {

        double sunAngleForAsr = -1.0 * Math.toDegrees(MathUtils.acot(times + Math.tan(Math.toRadians(Math.abs(latitude - sunDeclination(time))))));
        return getTimeBelowHorizonDifference(sunAngleForAsr);

    }

    public double getTimeBelowHorizonDifference(double angle){
        return getTimeBelowHorizonDifference(angle, time);
    }


    public double getTimeBelowHorizonDifference(double angle, double time){
        double topOperand = (-Math.sin(Math.toRadians(angle)))- Math.sin(Math.toRadians(latitude))* Math.sin(Math.toRadians(sunDeclination(time)));
        double bottomOperand = Math.cos(Math.toRadians(latitude))* Math.cos(Math.toRadians(sunDeclination(time)));
        double acos = Math.acos(topOperand / bottomOperand);
        if (Double.isNaN(acos))
            return acos;
        return (1.0/15.0)* Math.toDegrees(acos);
    }

    private static double sunDeclination(final double t) {
        final double e = Math.toRadians(obliquityCorrected(t));
        final double b = Math.toRadians(sunApparentLongitude(t));
        final double sint = Math.sin(e) * Math.sin(b);
        final double theta = Math.asin(sint);
        return Math.toDegrees(theta);
    }

    private static double obliquityCorrected(final double t) {
        final double e0 = meanObliquityOfEcliptic(t);
        final double omega = Math.toRadians(125.04 - 1934.136 * t);
        return e0 + 0.00256 * Math.cos(omega);
    }

    private static double meanObliquityOfEcliptic(final double t) {
        final double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
        return 23.0 + (26.0 + (seconds/60.0))/60.0;
    }

    private static double sunApparentLongitude(final double t) {
        final double omega = Math.toRadians(125.04 - 1934.136 * t);
        return sunTrueLongitude(t) - 0.00569 - 0.00478 * Math.sin(omega);
    }

    private static double sunTrueLongitude(final double t) {
        return sunGeometricMeanLongitude(t) + sunEquationOfCenter(t);
    }
    private static double sunGeometricMeanLongitude(final double t) {
        double L0 = 280.46646 + t*(36000.76983 + 0.0003032*t);
        L0 = L0 - 360* Math.floor(L0/360);
        return L0;
    }
    private static double sunEquationOfCenter(final double t) {
        final double m = Math.toRadians(sunGeometricMeanAnomaly(t));
        return Math.sin(1*m) * (1.914602 - t*(0.004817 + 0.000014*t)) +
                Math.sin(2*m) * (0.019993 - t*(0.000101             )) +
                Math.sin(3*m) * (0.000289);
    }

    private static double sunGeometricMeanAnomaly(final double t) {
        return 357.52911 + t * (35999.05029 - 0.0001537*t);
    }

    private static double equationOfTime(final double t) {
        double eps = Math.toRadians(obliquityCorrected(t));
        double l0  = Math.toRadians(sunGeometricMeanLongitude(t));
        double m   = Math.toRadians(sunGeometricMeanAnomaly(t));
        double e   = eccentricityEarthOrbit(t);
        double y   = Math.tan(eps/2);
        y *= y;

        double sin2l0 = Math.sin(2 * l0);
        double cos2l0 = Math.cos(2 * l0);
        double sin4l0 = Math.sin(4 * l0);
        double sin1m  = Math.sin(m);
        double sin2m  = Math.sin(2 * m);

        double etime = y*sin2l0 - 2*e*sin1m + 4*e*y*sin1m*cos2l0
                - 0.5*y*y*sin4l0 - 1.25*e*e*sin2m;

        return Math.toDegrees(etime)*4.0;
    }
    private static double eccentricityEarthOrbit(final double t) {
        return 0.016708634 - t*(0.000042037 + 0.0000001267*t);
    }

}
