package com.hodo.jjamtalk.Data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by boram on 2017-08-27.
 */

public class FanData implements Serializable, Comparable<FanData>{
    private static final long  serialVersionUID = 1L;

    public int Count;
    public String Nick;
    public String Idx;
    public String Img;

    @Override
    public int compareTo(@NonNull FanData fanData) {
        if (this.Count <  fanData.Count) {   // 내림차순 , 오름차순으로 하려면 < 으로~
            return -1;

        } else if (this.Count == fanData.Count) {
            return 0;
        } else {
            return 1;
        }
    }
}
