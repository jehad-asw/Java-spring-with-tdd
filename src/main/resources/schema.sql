CREATE TABLE IF NOT EXISTS Post(
    id int not null,
    user_id int not null,
    title varchar(250) not null,
    body text not null,
    version int,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Comment(
    id int not null,
    post_id int not null,
    name varchar(250) not null,
    email varchar(250) not null,
    body text not null,
    version int,
    PRIMARY KEY (id)
    );