CREATE TABLE followers_following(
    follower_id INTEGER NOT NULL REFERENCES users(id),
    followed_id INTEGER NOT NULL REFERENCES users(id)
);
CREATE index followers_following_index on followers_following(follower_id, followed_id);
