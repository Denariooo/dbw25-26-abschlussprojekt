CREATE TABLE adresse
(
    adresse_id serial PRIMARY KEY,
    aktiv      boolean        NOT NULL,
    strasse    varchar(60)    NOT NULL check (strasse ~ '^[A-ZÄÖÜ][a-zäöüßA-ZÄÖÜ]*$'),
    hausnummer varchar(10)    NOT NULL check (hausnummer ~ '^[0-9]+[a-z]?$'),
    plz        numeric(12, 0) NOT NULL,
    ort        varchar(100)   NOT NULL,
    land       varchar(100)   NOT NULL
);

CREATE TABLE kunde
(
    kunde_id serial PRIMARY KEY,
    email    varchar(256) NOT NULL UNIQUE check (email ~ '^[^@]+@[^@]+\.[^@]+$'),
    vorname  varchar(32)  NOT NULL check (vorname ~ '^[A-ZÄÖÜa-zäöüß]+$'),
    nachname varchar(32)  NOT NULL check (nachname ~ '^[A-ZÄÖÜa-zäöüß]+$'),
    passwort varchar(20)  NOT NULL check (
        LENGTH(passwort) BETWEEN 5 AND 20 AND
        passwort ~ '[A-Za-z]' AND
        passwort ~ '[0-9]' AND
        passwort ~ '[^A-Za-z0-9]'
        )
);

CREATE TABLE kunde_hat_adressen
(
    adresse_id int,
    kunde_id   int,
    typ        varchar(20) NOT NULL check (typ IN ('Rechnungsadresse', 'Lieferadresse')),
    PRIMARY KEY (adresse_id, kunde_id),
    FOREIGN KEY (adresse_id) REFERENCES adresse (adresse_id),
    FOREIGN KEY (kunde_id) REFERENCES kunde (kunde_id)
);

-- Für mitarbeiter.vorname/nachname wurden bewusst keine checks definiert,
-- da, hierfür in der Aufgabenstellung keine zusätzlichen Anforderungen gestellt wurden.
CREATE TABLE mitarbeiter
(
    personal_nr serial PRIMARY KEY,
    vorname     varchar(255) NOT NULL,
    nachname    varchar(255) NOT NULL,
    email       varchar(255) NOT NULL,
    passwort    varchar(20)  NOT NULL check (
        LENGTH(passwort) BETWEEN 5 AND 20 AND
        passwort ~ '[A-Za-z]' AND
        passwort ~ '[0-9]' AND
        passwort ~ '[^A-Za-z0-9]'
        )
);

CREATE TABLE bestellung
(
    bestellung_id     serial PRIMARY KEY,
    datum             timestamp   NOT NULL,
    status            varchar(20) NOT NULL check (status IN ('neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert')),
    mitarbeiterzuweis int         NOT NULL,
    kunde_id          int         NOT NULL,
    FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter (personal_nr),
    FOREIGN KEY (kunde_id) REFERENCES kunde (kunde_id)
);

CREATE TABLE produkt
(
    sku          varchar(255) PRIMARY KEY,
    name         varchar(255)   NOT NULL,
    preis        decimal(10, 2) NOT NULL,
    lagerbestand int            NOT NULL check (lagerbestand >= 0),
    angelegt_von int            NOT NULL,
    FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter (personal_nr)
);

CREATE TABLE bestellposition
(
    position_id   serial PRIMARY KEY,
    menge         int          NOT NULL check (menge > 0),
    sku           varchar(255) NOT NULL,
    bestellung_id int          NOT NULL,
    FOREIGN KEY (sku) REFERENCES produkt (sku),
    FOREIGN KEY (bestellung_id) REFERENCES bestellung (bestellung_id)
);

-- ============================================================
-- Trigger:
-- ============================================================

CREATE OR REPLACE FUNCTION check_status_storniert()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.status = 'storniert' AND OLD.status NOT IN ('neu', 'bezahlt') THEN
        RAISE EXCEPTION 'Status darf nur von neu oder bezahlt auf storniert gesetzt werden.';
    END IF;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_status_storniert
    BEFORE UPDATE OF status
    ON bestellung
    FOR EACH ROW
EXECUTE FUNCTION check_status_storniert();

-- bestellposition

CREATE OR REPLACE FUNCTION trg_bestellposition_insert() RETURNS TRIGGER AS
$$
DECLARE
    prod_lager  int;
    best_status varchar(20);
BEGIN
    select lagerbestand into prod_lager from produkt where sku = new.sku;
    select status into best_status from bestellung where bestellung_id = new.bestellung_id;

    if best_status = 'storniert' then
        return new;
    end if;

    if prod_lager < new.menge then
        raise exception 'Nicht genügend Lagerbestand für Produkt %', new.sku;
    end if;

    update produkt set lagerbestand = lagerbestand - new.menge where sku = new.sku;
    return new;
end
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bestellposition_update
    BEFORE UPDATE OF menge
    on bestellposition
    FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_insert();

--

CREATE OR REPLACE FUNCTION trg_bestellposition_delete() RETURNS TRIGGER AS
$$
declare
    best_status varchar(20);
begin
    select status into best_status from bestellung where bestellung_id = old.bestellung_id;

    if best_status = 'storniert' then
        return old;
    end if;

    update produkt set lagerbestand = lagerbestand + old.menge where sku = old.sku;
    return old;
end
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bestellposition_delete
    BEFORE DELETE
    ON bestellposition
    FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_delete();

--

CREATE OR REPLACE FUNCTION trg_bestellung_storniert() RETURNS TRIGGER AS
$$
begin
    if new.status = 'storniert' and old.status <> 'storniert' then
        update produkt p
        set lagerbestand = lagerbestand + bp.menge
        from bestellposition bp
        where bp.bestellung_id = new.bestellung_id
          AND p.sku = bp.sku;
    end if;
    return new;
end
$$ LANGUAGE plpgsql;

create trigger trg_bestellung_storniert
    after update of status
    on bestellung
    for each row
execute function trg_bestellung_storniert();

