package com.youximao.sdk.lib.common.common;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.youximao.sdk.lib.common.R;

import java.lang.reflect.Field;

//import com.gamecat.sdk.R;

/**
 * 根据资源的名字获取其ID值
 *
 * @author mining :http://my.csdn.net/xiaanming
 */
public class MResource {

    /**
     * @param context
     * @param classNameAddResId 例如 "R.drawable.xxx"
     * @return
     */
    public static int getIdByClassName(Context context, String classNameAddResId) {
        String[] resInfos = classNameAddResId.split(".");
        int id = 0;
        if (resInfos.length == 3) {
            switch (resInfos[1]) {
                case "drawable":
                    id = getIdByName(context, "drawable", resInfos[2]);
                    break;
                case "id":
                    id = getIdByName(context, "id", resInfos[2]);
                    break;
                case "layout":
                    id = getIdByName(context, "layout", resInfos[2]);
                    break;
            }
        }
        return id;
    }

    /**
     * 获取id
     *
     * @param name id的名字
     * @return
     */
    public static int getLayoutId(Context context, String name) {
        return MResource.getResource(context, "layout", name);
    }

    /**
     * 获取id
     *
     * @param name id的名字
     * @return
     */
    public static int getLayoutId(String name) {
        return MResource.getResource("layout", name);
    }

    /**
     * @param context
     * @param name
     * @return
     */
    public static int getIdId(Context context, String name) {
        return MResource.getResource(context, "id", name);
    }


    public static int getIdByName(Context context, String className, String name) {
        if (context != null) {
            String packageName = context.getPackageName();
            Class r = null;
            int id = 0;
            try {
                r = Class.forName(packageName + ".R");

                Class[] classes = r.getClasses();
                Class desireClass = null;

                for (int i = 0; i < classes.length; ++i) {
                    if (classes[i].getName().split("\\$")[1].equals(className)) {
                        desireClass = classes[i];
                        break;
                    }
                }

                if (desireClass != null) {
                    id = desireClass.getField(name).getInt(desireClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return id;
        }
        return 0;
    }


    public static int getResource(String className, String name) {
        Class desireClass = null;
        int resId = 0;
        switch (className) {
            case "drawable":
                desireClass = R.drawable.class;
                break;
            case "layout":
                desireClass = R.layout.class;
                break;
            case "id":
                desireClass = R.id.class;
                break;
            case "dimen":
                desireClass = R.dimen.class;
                break;

        }
        try {
            Field field = desireClass.getField(name);
            resId = field.getInt(name);
        } catch (NoSuchFieldException e) {//如果没有在"mipmap"下找到imageName,将会返回0
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resId == 0) {
            Log.e("Resource", "Resource not found & id == 0");
        }
        return resId;
    }


    public static int getResource(Context context, String type, String name) {
        try {
            String packageName = context.getPackageName();
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(name, type, packageName);
            return resId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int[] getIdsByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int[] ids = null;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null) && (desireClass.getField(name).get(desireClass).getClass().isArray()))
                ids = (int[]) desireClass.getField(name).get(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ids;
    }


}
