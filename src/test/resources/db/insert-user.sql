INSERT INTO public.users (email, password, name, roles)
VALUES ('asergeev@ya.ru', '$2a$12$cL6.w7/X.t4hI69CfwaTbuJgLBhV7exaZsl6ULD8pQZftA5yLsqbm', 'Irrelevant', '{ROLE_USER}')
ON CONFLICT DO NOTHING;