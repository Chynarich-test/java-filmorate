package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Data
@Builder
public class User implements Entity {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Map<Long, FriendshipStatus> friends;

    public Map<Long, FriendshipStatus> getFriends() {
        if (friends == null) {
            friends = new HashMap<>();
        }
        return friends;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

