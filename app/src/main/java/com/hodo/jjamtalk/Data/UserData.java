package com.hodo.jjamtalk.Data;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-05.
 */

public class UserData implements Serializable {

    private static final long  serialVersionUID = 1L;

    public String Idx;
    public String Img;
    public String NickName;
    public String Gender;
    public String Age;

    public double Lat;
    public double Lon;

    public int Heart;
    public int Hot;
    public int Rank;

    public String Date;

    public String Memo;
    public String School;
    public String Company;
    public String Title;
}
