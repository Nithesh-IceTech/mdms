do $$
DECLARE
    ICEOWNER varchar(40) := '_OWNER_';
    ICEUSER  varchar(40) := '_USERNAME_';
    ICEROLE  varchar(40) := '_ROLE_';
    R text := '';
BEGIN
    FOR R IN (SELECT tablename FROM pg_catalog.pg_tables WHERE tableowner = ICEOWNER)
        LOOP
            EXECUTE 'GRANT SELECT,UPDATE,DELETE,INSERT ON ' || R ||' TO '|| ICEROLE;
        END LOOP;
    RETURN;
END
$$ LANGUAGE plpgsql;