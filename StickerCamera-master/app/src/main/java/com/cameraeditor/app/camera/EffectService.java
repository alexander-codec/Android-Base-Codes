package com.cameraeditor.app.camera;

import com.cameraeditor.app.camera.effect.FilterEffect;
import com.cameraeditor.app.camera.util.GPUImageFilterTools;

import java.util.ArrayList;
import java.util.List;


public class EffectService {

    private static EffectService mInstance;

    public static EffectService getInst() {
        if (mInstance == null) {
            synchronized (EffectService.class) {
                if (mInstance == null)
                    mInstance = new EffectService();
            }
        }
        return mInstance;
    }

    private EffectService() {
    }

    public List<FilterEffect> getLocalFilters() {
        List<FilterEffect> filters = new ArrayList<FilterEffect>();
        filters.add(new FilterEffect("original", GPUImageFilterTools.FilterType.NORMAL, 0));

        filters.add(new FilterEffect("ambíguo", GPUImageFilterTools.FilterType.ACV_AIMEI, 0));
        filters.add(new FilterEffect("azul", GPUImageFilterTools.FilterType.ACV_DANLAN, 0));
        filters.add(new FilterEffect("gema", GPUImageFilterTools.FilterType.ACV_DANHUANG, 0));
        filters.add(new FilterEffect("retro", GPUImageFilterTools.FilterType.ACV_FUGU, 0));
        filters.add(new FilterEffect("frio", GPUImageFilterTools.FilterType.ACV_GAOLENG, 0));
        filters.add(new FilterEffect("nascente", GPUImageFilterTools.FilterType.ACV_HUAIJIU, 0));
        filters.add(new FilterEffect("filme", GPUImageFilterTools.FilterType.ACV_JIAOPIAN, 0));
        filters.add(new FilterEffect("bonitinho", GPUImageFilterTools.FilterType.ACV_KEAI, 0));
        filters.add(new FilterEffect("solitário", GPUImageFilterTools.FilterType.ACV_LOMO, 0));
        filters.add(new FilterEffect("fortalecer", GPUImageFilterTools.FilterType.ACV_MORENJIAQIANG, 0));
        filters.add(new FilterEffect("comove", GPUImageFilterTools.FilterType.ACV_NUANXIN, 0));
        filters.add(new FilterEffect("fresca", GPUImageFilterTools.FilterType.ACV_QINGXIN, 0));
        filters.add(new FilterEffect("sombras", GPUImageFilterTools.FilterType.ACV_RIXI, 0));
        filters.add(new FilterEffect("quente", GPUImageFilterTools.FilterType.ACV_WENNUAN, 0));

        return filters;
    }

}
