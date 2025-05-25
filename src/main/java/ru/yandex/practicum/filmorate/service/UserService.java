package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(Long userId1, Long userId2) {
        User user1 = inMemoryUserStorage.getOne(userId1);
        User user2 = inMemoryUserStorage.getOne(userId2);

        user1.getFriends().add(userId2);
        user2.getFriends().add(userId1);
    }

    public void deleteFriend(Long userId1, Long userId2) {
        User user1 = inMemoryUserStorage.getOne(userId1);
        User user2 = inMemoryUserStorage.getOne(userId2);

        user1.getFriends().remove(userId2);
        user2.getFriends().remove(userId1);
    }

    public List<User> showMutualFriends(Long userId1, Long userId2) {
        Set<Long> friends1 = inMemoryUserStorage.getOne(userId1).getFriends();
        Set<Long> friends2 = inMemoryUserStorage.getOne(userId2).getFriends();

        return friends1.stream()
                .filter(friends2::contains)
                .map(inMemoryUserStorage::getOne)
                .toList();
    }
}
