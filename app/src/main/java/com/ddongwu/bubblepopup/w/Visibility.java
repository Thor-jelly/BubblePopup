package com.ddongwu.bubblepopup.w;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/4/20 15:43 <br/>
 */
@RestrictTo(LIBRARY_GROUP)
@IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
@Retention(RetentionPolicy.SOURCE)
public @interface Visibility {}
