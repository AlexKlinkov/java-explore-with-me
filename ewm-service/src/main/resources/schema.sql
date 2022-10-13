drop table if exists "users" cascade;
drop table if exists "categories" cascade;
drop table if exists "compilations" cascade;
drop table if exists "participation_requests" cascade;
drop table if exists "events" cascade;
drop table if exists "comments" cascade;
drop table if exists "compilation_events" cascade;

CREATE TABLE "users"
(
    "id"    bigint generated always as identity not null primary key,
    "name"  varchar(100),
    "email" varchar(100)
);

CREATE TABLE "categories"
(
    "id"   bigint generated always as identity not null primary key,
    "name" varchar(100) unique
);

CREATE TABLE "compilations"
(
    "id"     bigint generated always as identity not null primary key,
    "pinned" boolean,
    "title"  varchar(120)
);

CREATE TABLE "participation_requests"
(
    "id"           bigint generated always as identity not null primary key,
    "event_id"     bigint,
    "created"      timestamp with time zone,
    "requestor_id" bigint,
    "status"       varchar(50)
);

CREATE TABLE "events"
(
    "id"                 bigint generated always as identity not null primary key,
    "annotation"         varchar(2000),
    "title"              varchar(120),
    "description"        varchar(7000),
    "category_id"        bigint,
    "initiator_id"       bigint,
    "confirmed_requests" bigint,
    "participant_limit"  bigint,
    "lat"                double precision,
    "lon"                double precision,
    "created_on"         timestamp with time zone,
    "published_on"       timestamp with time zone,
    "event_date"         timestamp with time zone,
    "state"              varchar(50),
    "paid"               boolean,
    "views"              bigint,
    "request_moderation" boolean
);

CREATE TABLE "comments"
(
    "id"        bigint generated always as identity not null primary key,
    "event_id"  bigint,
    "author_id" bigint,
    "comment"   varchar(1000),
    "created"   timestamp with time zone
);

CREATE TABLE "compilation_events"
(
    "compilation_id" bigint,
    "event_id"       bigint,
    PRIMARY KEY ("compilation_id", "event_id")
);

ALTER TABLE "participation_requests"
    ADD FOREIGN KEY ("event_id") REFERENCES "events" ("id");

ALTER TABLE "participation_requests"
    ADD FOREIGN KEY ("requestor_id") REFERENCES "users" ("id");

ALTER TABLE "events"
    ADD FOREIGN KEY ("category_id") REFERENCES "categories" ("id");

ALTER TABLE "events"
    ADD FOREIGN KEY ("initiator_id") REFERENCES "users" ("id");

ALTER TABLE "comments"
    ADD FOREIGN KEY ("event_id") REFERENCES "events" ("id");

ALTER TABLE "comments"
    ADD FOREIGN KEY ("author_id") REFERENCES "users" ("id");

ALTER TABLE "compilation_events"
    ADD FOREIGN KEY ("compilation_id") REFERENCES "compilations" ("id");

ALTER TABLE "compilation_events"
    ADD FOREIGN KEY ("event_id") REFERENCES "events" ("id");
