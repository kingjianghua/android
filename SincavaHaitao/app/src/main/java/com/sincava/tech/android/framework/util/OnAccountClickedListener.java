package com.sincava.tech.android.framework.util;

public interface OnAccountClickedListener {

    /**
     * Callback when an account is selected (clicked) from in a list of accounts
     * @param accountUID GUID of the selected account
     */
    public void accountSelected(String accountUID);
}
