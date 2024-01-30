package org.semenov.utils;

import org.semenov.model.User;

public abstract class QuestionHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
