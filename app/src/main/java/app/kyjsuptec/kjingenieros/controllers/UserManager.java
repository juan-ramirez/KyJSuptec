package app.kyjsuptec.kjingenieros.controllers;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static boolean isSesionActive(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getBoolean("mSesionActive", false);
    }

    public static String getUser(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mUser", "Default");
    }

    public static String getPassword(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getString("mPassword", "Default");
    }


    public static void setActiveSesion(Context mContext, String mUser, String mPassword) {
        SharedPreferences.Editor mSEditor = mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit();

        mSEditor.putBoolean("mSesionActive", true);
        mSEditor.putString("mUser", mUser);
        mSEditor.putString("mPassword", mPassword);
        mSEditor.commit();
    }

    public static void setInactiveSesion(Context mContext) {
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putBoolean("mSesionActive", false).commit();
    }

    public static void setIsAdmin(Context mContext, boolean isAdmin) {
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putBoolean("isAdmin", isAdmin).commit();
    }

    public static boolean getIsAdmin(Context mContext) {
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getBoolean("isAdmin", false);
    }

    public static void setUpLocalAdmin(Context mContext, String localAdminUser, String localAdminPassword) {
        SharedPreferences.Editor mSEditor = mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).edit();

        mSEditor.putString("localAdminUser", localAdminUser);
        mSEditor.putString("localAdminPassword", localAdminPassword);
        mSEditor.commit();
    }

    public static void setUpLocalUser(Context mContext, String localUserUser, String localUserPassword) {
        SharedPreferences.Editor mSEditor = mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).edit();

        mSEditor.putString("localUserUser", localUserUser);
        mSEditor.putString("localUserPassword", localUserPassword);
        mSEditor.commit();
    }

    public static void updateLocalAdminPassword(Context mContext, String newPassword) {
        mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).edit().putString("localAdminPassword", newPassword).commit();
    }

    public static void updateLocalUserPassword(Context mContext, String newPassword) {
        mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).edit().putString("localUserPassword", newPassword).commit();
    }

    public static String getLocalAdminPassword(Context mContext) {
        return mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).getString("localAdminPassword","Default");
    }

    public static String getLocalUserPassword(Context mContext) {
        return mContext.getSharedPreferences("mLocalUsersData", Context.MODE_PRIVATE).getString("localUserPassword","Default");
    }

    public static int getCurrentMemoExpresNumber(Context mContext){
        return mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).getInt("mMemoExpresNumber",1);
    }

    public static void updateCurrentMemoExpresNumber(Context mContext){
        int current = getCurrentMemoExpresNumber(mContext);
        mContext.getSharedPreferences("mUserData", Context.MODE_PRIVATE).edit().putInt("mMemoExpresNumber",current+1).commit();
    }
}
