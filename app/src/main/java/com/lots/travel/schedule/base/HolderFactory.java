package com.lots.travel.schedule.base;

import android.view.ViewGroup;

/**
 * Created by nalanzi on 2017/12/25.
 */

public interface HolderFactory<T extends ScheduleAdapter> {
    ScheduleHolder getHolder(T adapter, ViewGroup parent, int childType);
}
