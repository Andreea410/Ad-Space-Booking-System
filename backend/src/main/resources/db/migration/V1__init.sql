CREATE TABLE ad_space (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          type VARCHAR(50) NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          price_per_day NUMERIC(10,2) NOT NULL CHECK (price_per_day > 0),
                          status VARCHAR(50) NOT NULL,
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
                                 status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                 total_cost NUMERIC(10, 2) NOT NULL CHECK (total_cost > 0),
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                                 CONSTRAINT chk_booking_dates
                                     CHECK (end_date > start_date),

                                 CONSTRAINT chk_minimum_duration
                                     CHECK (end_date >= start_date + INTERVAL '7 days')
    );

CREATE INDEX idx_booking_status ON booking_request(status);
CREATE INDEX idx_booking_adspace ON booking_request(ad_space_id);

CREATE INDEX idx_booking_date_range
    ON booking_request(ad_space_id, start_date, end_date, status)
    WHERE status IN ('PENDING', 'APPROVED');
