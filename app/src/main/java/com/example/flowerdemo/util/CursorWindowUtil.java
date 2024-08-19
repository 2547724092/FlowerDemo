package com.example.flowerdemo.util;

import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 :
 */
public class CursorWindowUtil {
    public static void cw(Cursor cursor) {
        CursorWindow cw = new CursorWindow("test", 900000000);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) cursor;
        ac.setWindow(cw);
    }
}
