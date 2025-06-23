package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newElement) {
        return userStorage.update(newElement);
    }

    public User getOne(Long id) {
        return userStorage.getOne(id);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.getOne(userId);
        userStorage.getOne(friendId);

        userStorage.addFriendship(userId, friendId, FriendshipStatus.FRIENDS);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.getOne(userId);
        userStorage.getOne(friendId);

        userStorage.removeFriendship(userId, friendId);
    }

    public List<User> showMutualFriends(Long userId1, Long userId2) {
        return userStorage.getMutualFriends(userId1, userId2);
    }
}
