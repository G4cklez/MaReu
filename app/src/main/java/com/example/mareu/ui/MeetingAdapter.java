package com.example.mareu.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.model.Meeting;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter <MeetingAdapter.ViewHolder> {

    List<Meeting> mMeetingList;
    LayoutInflater mLayoutInflater;
    DeleteMeetingListener mDeleteMeetingListener;

    public MeetingAdapter(List<Meeting> mMeetingList, DeleteMeetingListener mDeleteMeetingListener) {
        this.mMeetingList = mMeetingList;
        this.mDeleteMeetingListener = mDeleteMeetingListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mMeetingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
        }

        void bindData(final int position) {
            AppCompatImageView roundShapeMeeting = mView.findViewById(R.id.round_shape_meeting);
            TextView tvMeeting = mView.findViewById(R.id.tv_meeting);
            TextView tvParticipants = mView.findViewById(R.id.tv_participants);
            AppCompatImageView deleteBtn = mView.findViewById(R.id.delete_btn);
            tvMeeting.setText(mMeetingList.get(position).getRoomName()
                    + " - " + mMeetingList.get(position).getFormattedTime()
                    + " - " + mMeetingList.get(position).getSubject());

            String users = "";
            for (String mUser : mMeetingList.get(position).getParticipantsList()) {
                if(users.isEmpty()){
                    users = mUser;
                }else
                users +=  ", "+ mUser;
            }
            tvParticipants.setText(users);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteMeetingListener.deleteMeeting(mMeetingList.get(position));
                }
            });

            roundShapeMeeting.setColorFilter(mMeetingList.get(position).getColor());
        }
    }

    interface DeleteMeetingListener {
        void deleteMeeting(Meeting meeting);
    }
}
