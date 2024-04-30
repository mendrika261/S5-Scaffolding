CREATE TABLE region (
    region_id serial PRIMARY KEY,
    region_description character varying(60) NOT NULL
);

CREATE TABLE territorie (
    territory_id serial PRIMARY KEY,
    territory_description character varying(60) NOT NULL,
    region_id integer NOT NULL REFERENCES region (region_id)
);
