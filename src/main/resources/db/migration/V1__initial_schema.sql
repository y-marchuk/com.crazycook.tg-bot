CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         customer_chatId INTEGER
);

CREATE TABLE customer (
                        chatId SERIAL PRIMARY KEY,
                        username varchar
);