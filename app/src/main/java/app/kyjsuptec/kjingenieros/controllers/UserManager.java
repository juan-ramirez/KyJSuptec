package app.kyjsuptec.kjingenieros.controllers;

import android.content.Context;

/**
 * Created by USER on 15/03/2015.
 */
public class UserManager {

    public static String getCurrentUser(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mCurrentUser", "Default");
    }

    public static String getProyecto(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mProyecto", "Default");
    }

    public static String getReviso(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mReviso", "Default");
    }

    public static String getAprobo(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mAprobo", "Default");
    }

    public static void setProyecto(Context mContext, String mProyecto) {
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putString("mProyecto", mProyecto).commit();
    }

    public static void setReviso(Context mContext, String mReviso) {
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putString("mReviso", mReviso).commit();
    }

    public static void setAprobo(Context mContext, String mAprobo) {
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putString("mAprobo", mAprobo).commit();
    }


}
