CREATE OR REPLACE FUNCTION get_external_transaction_direction(category_id bigint) RETURNS int AS
$BODY$
DECLARE
    category_type category_type;
    direction     int;
BEGIN
    category_type := (SELECT type
                      FROM categories c
                               JOIN category_groups cg on c.category_group_id = cg.id
                      WHERE c.id = category_id);

    if category_type = 'income' then
        direction := 1;
    elsif category_type = 'expense' then
        direction := -1;
    else
        raise 'Unsupported operation type %', category_type;
    end if;

    return direction;
END;
$BODY$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION on_external_transaction_change() RETURNS TRIGGER AS
$BODY$
DECLARE
    amount         numeric;
    direction      int;
    balance_change numeric;
    account_id bigint;
BEGIN
    amount := coalesce(new.amount, 0) - coalesce(old.amount, 0);
    direction := get_external_transaction_direction(coalesce(new.category_id, old.category_id));
    balance_change := amount * direction;
    account_id = coalesce(new.account_id, old.account_id);

    raise notice 'new amount: %, old amount: %, amount: %, direction: %, balance change: %, account: %', new.amount, old.amount, amount, direction, balance_change, account_id;

    UPDATE accounts a
    SET balance = balance + balance_change
    WHERE a.id = account_id;

    RETURN new;
END;
$BODY$
    LANGUAGE plpgsql;
