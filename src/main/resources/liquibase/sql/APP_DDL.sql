create table products
(
    product_id uuid primary key default gen_random_uuid(),
    name       text          not null,
    price      numeric(9, 2) not null
);

create table discounts
(
    discount_id   serial primary key,
    product_id    uuid not null references products (product_id),
    amount_factor numeric(8, 2),
    percentage    integer,
    check ( -- either amount or percentage based discount type
            (amount_factor is not null and percentage is null)
            or (amount_factor is null and percentage is not null)
        )
);

create index discounts_product_id_idx on discounts (product_id);
