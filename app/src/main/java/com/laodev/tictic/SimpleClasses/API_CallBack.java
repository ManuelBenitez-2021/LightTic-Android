package com.laodev.tictic.SimpleClasses;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/4/2019.
 */

public interface API_CallBack {

    void ArrayData(ArrayList arrayList);

    void OnSuccess(String responce);

    void OnFail(String responce);


}
