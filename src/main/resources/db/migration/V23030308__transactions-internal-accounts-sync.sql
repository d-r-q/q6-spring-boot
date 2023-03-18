-- Опции:
-- 1.	Добавление новой
-- a.	У источника отнять
-- b.	У цели добавить
-- 2.	Удаление старой
-- a.	К источнику добавить
-- b.	У цели отнять
-- 3.	Изменение суммы
-- a.	У источника отнять разницу (старое – новое)
-- b.	К цели добавить разницу
-- 4.	Изменение источника
-- a.	К изначальному источинку добавить изначальное кол-во
-- b.	У нового источника отнять новое кол-во
-- 5.	Изменение цели
-- a.	У изначальной цели отнять кол-во
-- b.	К новой цели добавить кол-во


CREATE OR REPLACE FUNCTION on_internal_transaction_change() RETURNS TRIGGER AS
$BODY$
DECLARE
    balance_change numeric;
    operation     varchar;
BEGIN
    operation := case
                     when (new.amount is not null and old.amount is null) then 'insert'
                     when new.amount is null and old.amount is not null then 'delete'
                     else 'update'
        end;

    balance_change := coalesce(new.amount, 0) - coalesce(old.amount, 0);

    if (operation = 'insert') then
        UPDATE accounts a
        SET balance = balance - balance_change
        WHERE a.id = a.source_account_id;

        UPDATE accounts a
        SET balance = balance + balance_change
        WHERE a.id = a.target_account_id;
    elsif (opertation = 'update') then
        UPDATE accounts a
        SET balance = balance + balance_change
        WHERE a.id = a.source_account_id;

        UPDATE accounts a
        SET balance = balance - balance_change
        WHERE a.id = a.target_account_id;
    else
        RAISE 'updates isn''t supported yet';
    end if;

    RETURN new;
END;
$BODY$
    LANGUAGE plpgsql;
