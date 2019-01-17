//package com.creativeapps.salat_times.UnusedClassesUseful;
//
///**
// * Created by Sifat Ullah on 5/7/2018.
// */
//
///*
// *  This program is free software; you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License version 2 as
// *  published by the Free Software Foundation
// *
// *  This program is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// *
// *  You should have received a copy of the GNU General Public License
// *  along with this program; if not, write to the Free Software
// *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
// *
// * MobiMoon 2.0 <info@mypapit.net>
// * Copyright 2007 Mohammad Hafiz bin Ismail. All rights reserved.
// *
// * HijriConvert.java
// * For converting Gregorian to Hijri dates
// */
//
//
//
//
//public class HijriConvert {
//    private int day,month,year,phase;
//
//
//    private int intPart(double value)
//    {
//		/*if (value < -0.001){
//	 		return Math.ceil(value -0.001);
//		}
//		return Math.floor(value +0.001);
//		*/
//        return (int) value;
//
//    }
//    public HijriConvert(int d, int m, int y) {
//
//        double l,n,jd,j;
//
//        moonPhase(y,m,d);
//
//
//		/*if ((y>1582)||((y==1582)&&(m>10))||((y==1582)&&(m==10)&&(d>14)))
//		{
//			jd=intPart((1461*(y+4800+intPart((m-14)/12)))/4)+intPart((367*(m-2-12*(intPart((m-14)/12))))/12)-
//			intPart( (3* (intPart(  (y+4900+    intPart( (m-14)/12)     )/100)    )   ) /4)+d-32075;
//		} else	{
//			jd = 367*y-intPart((7*(y+5001+intPart((m-9)/7)))/4)+intPart((275*m)/9)+d+1729777;
//		}
//
//		jd=(1461*(y+4800+(m-14)/12))/4+367*(m-2-12*(((m-14)/12)))/12-
//		 (3* ( (y+4900+    (m-14)/12     )/100    )   ) /4+d-32075;
//		*/
//
//        jd = (1461 * (y + 4800 + (m - 14) / 12)) / 4 +
//                (367 * (m - 2 - 12 * ((m - 14) / 12))) / 12 -
//                (3 * ((y + 4900 + (m - 14) / 12) / 100 )) / 4 +
//                d - 32075;
//        //arg.JD.value=jd
//        //arg.wd.value=weekDay(jd%7)
//		/*l=jd-1948440+10632;
//		n=intPart((l-1)/10631);
//		l=l-10631*n+354;
//		j=(intPart((10985-l)/5316))*(intPart((50*l)/17719))+(intPart(l/5670))*(intPart((43*l)/15238));
//		l=l-(intPart((30-j)/15))*(intPart((17719*j)/50))-(intPart(j/16))*(intPart((15238*j)/43))+29;
//		this.month=(int) intPart((24*l)/709);
//		this.day=(int) (l- intPart((709*this.month)/24));
//		this.year=(int) (30*n+j-30);
//		*/
//
//        l=jd-1948440+10632;
//        n = (int) ((l- 1) / 10631);
//        l = l- 10631 * n + 354;
//        j = ((int) ((10985 - l) / 5316)) *
//                ((int) (50 * l/ 17719)) +
//                ((int) (l / 5670)) *
//                        ((int) (43 * l / 15238));
//
//        l = l- ((int) ((30 - j) / 15)) *
//                ((int) ((17719 * j) / 50)) -
//                ((int) (j / 16)) *
//                        ((int) ((15238 * j) / 43)) + 29;
//        this.month = (int) (24 * l / 709);
//        this.day = (int) (   l - (int) (709 * this.month / 24)   );
//        this.year = (int) (30 * n + j - 30);
//
//    }
//
//
//
//
//
//    public int getDay()
//    {
//        return day;
//    }
//
//    public int getMonth()
//    {
//        return month;
//    }
//
//    public int getYear()
//    {
//        return year;
//    }
//
//    public void calibrate(int value)
//    {
//        day = day + value;
//        if (day > 30) {
//            day = day % 30;
//            month = month +1;
//        } else if (day < 1) {
//            day = (29+day);
//            month = month -1;
//        }
//    }
//
//    private void moonPhase(int y, int m, int d)
//    {
//            /*
//              calculates the moon phase (0-7), accurate to 1 segment.
//              0 = > new moon.
//              4 => full moon.
//              */
//
//        double c,e;
//        double jd;
//        int b;
//
//        if (m < 3) {
//            y--;
//            m += 12;
//        }
//        ++m;
//        c =  365.25*y;
//        e =  30.6*m;
//        jd = c+e+d-694039.09;  /* jd is total days elapsed */
//        jd /= 29.53;           /* divide by the moon cycle (29.53 days) */
//        b = (int) jd;              /* int(jd) -> b, take integer part of jd */
//        jd -= b;                   /* subtract integer part to leave fractional part of original jd */
//        b = (int) (jd*8 + 0.5);    /* scale fraction from 0-8 and round by adding 0.5 */
//        b = b & 7;                 /* 0 and 8 are the same so turn 8 into 0 */
//
//        this.phase =b;
//    }
//
//    public int getPhase(){
//        return this.phase;
//    }
//
//
//
//}