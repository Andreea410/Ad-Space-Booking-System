CREATE TYPE ad_space_type AS ENUM (
    'BILLBOARD',
    'BUS_STOP',
    'MALL_DISPLAY',
    'TRANSIT_AD'
);

CREATE TYPE ad_space_status AS ENUM (
    'AVAILABLE',
    'BOOKED',
    'MAINTENANCE'
);

CREATE TYPE booking_status AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED'
);


CREATE TABLE ad_space (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          type ad_space_type NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          price_per_day NUMERIC(10,2) NOT NULL,
                          status ad_space_status NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_adspace_type ON ad_space(type);
CREATE INDEX idx_adspace_city ON ad_space(city);

CREATE TABLE booking_request (
                                 id BIGSERIAL PRIMARY KEY,
                                 ad_space_id BIGINT NOT NULL REFERENCES ad_space(id),
                                 advertiser_name VARCHAR(255) NOT NULL,
                                 advertiser_email VARCHAR(255) NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL,
                                 status booking_status NOT NULL,
                                 total_cost NUMERIC(10,2) NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_booking_status ON booking_request(status);
CREATE INDEX idx_booking_adspace ON booking_request(ad_space_id);
