package com.dhnsoft.testandroid.utils;

public class MemoryUtil {

    /**
     * 是否刷新第一个界面
     */
    public boolean isRefreshMain = false;

    /**
     * 是否刷新第二个界面
     */
    public boolean isRefreshSecond = false;

    /**
     * 是否刷新第三个界面
     */
    public boolean isRefreshThree = false;

    private static MemoryUtil mMS;
    public static MemoryUtil MS = MemoryUtil.getIntance();

    private static MemoryUtil getIntance() {
        if (mMS == null) mMS = new MemoryUtil();
        return mMS;
    }

    /**
     * 清理保存的内存
     */
    public static void clearMomery() {
        mMS = null;
    }
}