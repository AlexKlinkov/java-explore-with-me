drop table if exists endpoints_hit cascade;

CREATE TABLE "endpoints_hit" (
    "id"            bigint generated always as identity not null primary key,
    "app"           varchar(100),
    "attributes"    jsonb,
    "timestamp"     timestamp
);
