package com.example.warre.anuzo;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private static final String LOG_TAG = "FriendsFragment";

    private ArrayList<String> nicknames, names;
    private ArrayList<Boolean> editing;

    private ExpandableListView expandableListView;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        nicknames = new ArrayList<>();
        names = new ArrayList<>();
        editing = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            nicknames.add("abc " + i);
            names.add("def " + i);
            editing.add(false);
        }
        FriendsExpandableListAdapter adapter = new FriendsExpandableListAdapter();
        expandableListView = ((ExpandableListView) rootView.findViewById(R.id.friends_list));
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
//        expandableListView.setGroupIndicator(null);
        return rootView;
    }

    private class FriendsExpandableListAdapter extends BaseExpandableListAdapter {

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return names.get(groupPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_child, parent, false);
            }

            final String nickname = nicknames.get(groupPosition), name = names.get(groupPosition);
            final TextView nameView = convertView.findViewById(R.id.name);
            nameView.setText(name);
            final EditText editName = convertView.findViewById(R.id.edit_nickname);
            editName.setText(nickname);
//            editName.requestFocus();
//            editName.setSelection(editName.length());
            nameView.requestFocus();
            editName.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editName.setImeOptions(EditorInfo.IME_ACTION_GO);
            editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO || event != null && (actionId == EditorInfo.IME_ACTION_SEND || event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                        nicknames.set(groupPosition, v.getText().toString());
                        InputMethodManager imm =
                                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
                        nameView.requestFocus();
                        notifyDataSetChanged();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
//            editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    Log.e("except", hasFocus+"");
//                    if (!hasFocus) {
//                        InputMethodManager imm =
//                                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
//                    }
//                }
//            });
            Button remove_friend = convertView.findViewById(R.id.remove_friend);
            remove_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nicknames.remove(groupPosition);
                    names.remove(groupPosition);
                    expandableListView.collapseGroup(groupPosition);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return nicknames.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return nicknames.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_group, parent, false);
            }
            TextView nickname = convertView.findViewById(R.id.nickname);
            nickname.setText(nicknames.get(groupPosition));
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return nicknames.isEmpty();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
