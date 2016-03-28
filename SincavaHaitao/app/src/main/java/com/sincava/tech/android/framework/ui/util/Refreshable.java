package com.sincava.tech.android.framework.ui.util;

public interface Refreshable {

    /**
     * Refresh the list, typically be restarting the loader
     */
    public void refresh();

    /**
     * Refresh the list with modified parameters
     * @param uid GUID of relevant item to be refreshed
     */
    public void refresh(String uid);
}
