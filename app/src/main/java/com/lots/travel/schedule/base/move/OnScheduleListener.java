package com.lots.travel.schedule.base.move;

/**
 * Created by nalanzi on 2017/12/26.
 */

public interface OnScheduleListener {
    void onAdd(int flatPosition);
    void onDelete(int flatPosition);
}
