ALTER TABLE booking_request
    ALTER COLUMN status SET DEFAULT 'PENDING';

ALTER TABLE booking_request
    ADD CONSTRAINT chk_booking_dates 
    CHECK (end_date > start_date);

ALTER TABLE booking_request
    ADD CONSTRAINT chk_minimum_duration
    CHECK (end_date >= start_date + INTERVAL '7 days');

ALTER TABLE booking_request
    ADD CONSTRAINT chk_positive_cost
    CHECK (total_cost > 0);

ALTER TABLE ad_space
    ADD CONSTRAINT chk_positive_price
    CHECK (price_per_day > 0);

CREATE INDEX idx_booking_date_range 
    ON booking_request(ad_space_id, start_date, end_date, status)
    WHERE status IN ('PENDING', 'APPROVED');

COMMENT ON CONSTRAINT chk_booking_dates ON booking_request IS
    'Ensures end date is after start date';
COMMENT ON CONSTRAINT chk_minimum_duration ON booking_request IS 
    'Ensures minimum booking duration of 7 days';

