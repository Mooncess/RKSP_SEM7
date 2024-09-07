package practice3.task3;

import io.reactivex.Observable;
public class UserFriend {
    private final int userId;
    private final int friendId;
    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public int getUserId() {
        return userId;
    }
    public int getFriendId() {
        return friendId;
    }
    public static Observable<UserFriend> getFriends(int userId) {
        return Observable.fromArray(
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100))
        );
    }
    public static void main(String[] args) {
        Integer[] userIdArray = {1, 2, 3, 4, 5};
        Observable<Integer> userIdStream = Observable.fromArray(userIdArray);

        Observable<UserFriend> userFriendStream = userIdStream
                .flatMap(userId -> getFriends(userId));

        userFriendStream.subscribe(userFriend -> {
            System.out.println("User: " + userFriend.getUserId() + ", Friend: "
                    + userFriend.getFriendId());
        });
    }
}

