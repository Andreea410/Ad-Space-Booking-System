INSERT INTO ad_space (name, type, city, address, price_per_day, status)
VALUES
    ('City Center Billboard',      'BILLBOARD',  'Bucharest', 'Piata Universitatii',       150.00, 'AVAILABLE'),
    ('Old Town Bus Stop',          'BUS_STOP',   'Bucharest', 'Strada Lipscani 23',        75.00,  'AVAILABLE'),
    ('Mall Entrance LED Screen',   'MALL_DISPLAY','Cluj',     'Iulius Mall, Entrance A',   220.00, 'AVAILABLE'),
    ('Train Station Transit Ad',   'TRANSIT_AD', 'Timisoara', 'Gara de Nord Platform 1',   180.00, 'AVAILABLE'),
    ('Suburban Billboard',         'BILLBOARD',  'Brasov',    'DN1 Km 150',                90.00,  'AVAILABLE');


INSERT INTO booking_request (
    ad_space_id,
    advertiser_name,
    advertiser_email,
    start_date,
    end_date,
    status,
    total_cost
) VALUES
      (1, 'Acme Corp',          'contact@acme.com',       DATE '2025-12-01', DATE '2025-12-15', 'APPROVED', 2100.00),
      (2, 'Local Coffee Shop',  'promo@coffee.local',     DATE '2025-11-10', DATE '2025-11-24', 'PENDING',  1050.00),
      (3, 'Tech Conference',    'marketing@techexpo.io',  DATE '2026-01-05', DATE '2026-01-19', 'PENDING',  3080.00),
      (4, 'Sports Brand',       'ads@sportbrand.com',     DATE '2025-12-20', DATE '2026-01-05', 'REJECTED', 2880.00);
