package com.hodo.talkking.Data;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-14.
 */

public class SendData implements Serializable {

    private static final long  serialVersionUID = 1L;

    public String strFireBaseKey;
    public String strTargetNick;
    public String strTargetImg;
    public String strTargetMsg;
    public String strSendName;
    public String strSendDate;
    public int nSendHoney;

}
