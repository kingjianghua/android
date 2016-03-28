package com.sincava.tech.android.framework.ui.account;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincava.tech.android.R;
import com.sincava.tech.android.framework.ui.util.Refreshable;
import com.sincava.tech.android.framework.ui.util.widget.EmptyRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountsListFragment extends Fragment implements Refreshable {

    AccountRecyclerAdapter mAccountRecyclerAdapter;
    @Bind(R.id.account_recycler_view)
    EmptyRecyclerView mRecyclerView;
    @Bind(R.id.empty_view) TextView mEmptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accounts_list, container, false);

        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(mEmptyTextView);

        switch (mDisplayMode) {

            case TOP_LEVEL:
                mEmptyTextView.setText(R.string.label_no_accounts);
                break;
            case RECENT:
                mEmptyTextView.setText(R.string.label_no_recent_accounts);
                break;
            case FAVORITES:
                mEmptyTextView.setText(R.string.label_no_favorite_accounts);
                break;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
        return v;
    }


    @Override
    public void refresh() {

    }

    @Override
    public void refresh(String uid) {

    }

    class AccountRecyclerAdapter extends CursorRecyclerAdapter<AccountRecyclerAdapter.AccountViewHolder> {

        public AccountRecyclerAdapter(Cursor cursor){
            super(cursor);
        }

        @Override
        public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_account, parent, false);

            return new AccountViewHolder(v);
        }

        @Override
        public void onBindViewHolderCursor(final AccountViewHolder holder, final Cursor cursor) {
            final String accountUID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_UID));
            holder.accoundId = mAccountsDbAdapter.getID(accountUID);

            holder.accountName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_NAME)));
            int subAccountCount = mAccountsDbAdapter.getSubAccountCount(accountUID);
            if (subAccountCount > 0) {
                holder.description.setVisibility(View.VISIBLE);
                String text = getResources().getQuantityString(R.plurals.label_sub_accounts, subAccountCount, subAccountCount);
                holder.description.setText(text);
            } else
                holder.description.setVisibility(View.GONE);

            // add a summary of transactions to the account view
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Make sure the balance task is truely multithread
                new AccountBalanceTask(holder.accountBalance).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, accountUID);
            } else {
                new AccountBalanceTask(holder.accountBalance).execute(accountUID);
            }
            String accountColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_COLOR_CODE));
            int colorCode = accountColor == null ? Color.TRANSPARENT : Color.parseColor(accountColor);
            holder.colorStripView.setBackgroundColor(colorCode);

            boolean isPlaceholderAccount = mAccountsDbAdapter.isPlaceholderAccount(accountUID);
            if (isPlaceholderAccount) {
                holder.createTransaction.setVisibility(View.GONE);
            } else {
                holder.createTransaction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
                        intent.putExtra(UxArgument.SELECTED_ACCOUNT_UID, accountUID);
                        intent.putExtra(UxArgument.FORM_TYPE, FormActivity.FormType.TRANSACTION.name());
                        getActivity().startActivity(intent);
                    }
                });
            }

            if (mAccountsDbAdapter.isFavoriteAccount(accountUID)){
                holder.favoriteStatus.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                holder.favoriteStatus.setImageResource(R.drawable.ic_star_border_black_24dp);
            }

            holder.favoriteStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isFavoriteAccount = mAccountsDbAdapter.isFavoriteAccount(accountUID);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseSchema.AccountEntry.COLUMN_FAVORITE, !isFavoriteAccount);
                    mAccountsDbAdapter.updateRecord(accountUID, contentValues);

                    int drawableResource = !isFavoriteAccount ?
                            R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp;
                    holder.favoriteStatus.setImageResource(drawableResource);
                    if (mDisplayMode == DisplayMode.FAVORITES)
                        refresh();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListItemClick(accountUID);
                }
            });
        }


        class AccountViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{
            @Bind(R.id.primary_text) TextView accountName;
            @Bind(R.id.secondary_text) TextView description;
            @Bind(R.id.account_balance) TextView accountBalance;
            @Bind(R.id.create_transaction) ImageView createTransaction;
            @Bind(R.id.favorite_status) ImageView favoriteStatus;
            @Bind(R.id.options_menu) ImageView optionsMenu;
            @Bind(R.id.account_color_strip) View colorStripView;
            long accoundId;

            public AccountViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                optionsMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getActivity(), v);
                        popup.setOnMenuItemClickListener(AccountViewHolder.this);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.account_context_menu, popup.getMenu());
                        popup.show();
                    }
                });

            }


            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.context_menu_edit_accounts:
                        openCreateOrEditActivity(accoundId);
                        return true;

                    case R.id.context_menu_delete:
                        tryDeleteAccount(accoundId);
                        return true;

                    default:
                        return false;
                }
            }
        }
    }
}
