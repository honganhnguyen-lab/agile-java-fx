package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;

import java.util.ArrayList;
import java.util.List;

public class UserStoryStateManager {

    public static List<String> getUserStories() {
        List<String> userStories = new ArrayList<>();
        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            userStories.add(userStory.toString());
        }
        return userStories;
    }

    public static void updateUserStoryStatus(String userStoryDescription, String newStatus) {
        List<UserStory> userStories = UserStoryStore.getInstance().getUserStories();
        for (UserStory userStory : userStories) {
            if (userStory.toString().equals(userStoryDescription)) {
                userStory.setStoryStatus(newStatus);
                break;
            }
        }
    }

    public static void deleteUserStory(String userStoryDescription) {
        List<UserStory> userStories = UserStoryStore.getInstance().getUserStories();
        for (UserStory userStory : userStories) {
            if (userStory.toString().equals(userStoryDescription)) {
                UserStoryStore.getInstance().deleteUserStory(userStory);
                break;
            }
        }
    }
}
