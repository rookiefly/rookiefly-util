package com.rookiefly.commons.collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserActivity implements Comparable<UserActivity> {

    private String userName;

    private Integer activity;

    @Override
    public int compareTo(UserActivity userActivity) {
        if (activity > userActivity.getActivity()) {
            return 1;
        } else if (activity < userActivity.getActivity()) {
            return -1;
        }
        return 0;
    }
}
