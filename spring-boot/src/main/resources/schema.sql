CREATE TABLE adresse
(
    adresse_id serial PRIMARY KEY,
    aktiv      boolean        NOT NULL,
    -- Für strasse wurde angenommen, dass tatsächlich nur reine Buchstabenfolgen ohne Leerzeichen/Bindestrich gemeint sind
    strasse    varchar(60)    NOT NULL check (strasse ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
    hausnummer varchar(10)    NOT NULL check (hausnummer ~ '^[0-9]+[a-z]?$'),
    plz        numeric(12, 0) NOT NULL,
    ort        varchar(100)   NOT NULL,
    land       varchar(100)   NOT NULL
);

CREATE TABLE kunde
(
    kunde_id serial PRIMARY KEY,
    -- Stellt sicher für email: @ vor dem Punkt (Anforderung)
    -- Zusätzlich: Das vor/nach den Symbolen Zeichen stehen, um invalide Formate wie "@.de" oder "test@." auszuschließen
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
    FOREIGN KEY (adresse_id) REFERENCES adresse (adresse_id) ON DELETE CASCADE,
    FOREIGN KEY (kunde_id) REFERENCES kunde (kunde_id) ON DELETE CASCADE
);

-- Für mitarbeiter.vorname/nachname wurden bewusst keine checks definiert,
-- da, hierfür in der Aufgabenstellung keine zusätzlichen Anforderungen gestellt wurden.
-- Ebenso wurde nicht das unique constraint für email hinzugefügt, da dies nicht explizit gefordert wurde.
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
    datum             timestamp WITH TIME ZONE NOT NULL,
    status            varchar(20)              NOT NULL check (status IN ('neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert')),
    mitarbeiterzuweis int                      NOT NULL,
    kunde_id          int                      NOT NULL,
    FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter (personal_nr) ON DELETE CASCADE,
    FOREIGN KEY (kunde_id) REFERENCES kunde (kunde_id) ON DELETE CASCADE
);

CREATE TABLE produkt
(
    sku          varchar(255) PRIMARY KEY,
    name         varchar(255)   NOT NULL,
    preis        decimal(10, 2) NOT NULL,
    lagerbestand int            NOT NULL check (lagerbestand >= 0),
    angelegt_von int            NOT NULL,
    FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter (personal_nr) ON DELETE CASCADE
);

CREATE TABLE bestellposition
(
    position_id   serial PRIMARY KEY,
    menge         int          NOT NULL check (menge > 0),
    sku           varchar(255) NOT NULL,
    bestellung_id int          NOT NULL,
    FOREIGN KEY (sku) REFERENCES produkt (sku) ON DELETE CASCADE,
    FOREIGN KEY (bestellung_id) REFERENCES bestellung (bestellung_id) ON DELETE CASCADE
);

-- ============================================================
-- Trigger:
-- ============================================================

-- bestellung

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

CREATE TRIGGER trg_bestellposition_insert
    BEFORE INSERT
    ON bestellposition
    FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_insert();

--

CREATE OR REPLACE FUNCTION trg_bestellposition_update() RETURNS TRIGGER AS
$$
DECLARE
    diff        int;
    prod_lager  int;
    best_status varchar(20);
BEGIN
    select status into best_status from bestellung where bestellung_id = new.bestellung_id;

    if best_status = 'storniert' then
        return new;
    end if;

    diff := new.menge - old.menge;

    select lagerbestand into prod_lager from produkt where sku = new.sku;

    if diff > 0 AND prod_lager < diff then
        RAISE EXCEPTION 'Nicht genügend Lagerbestand für Produkt %', new.sku;
    end if;

    UPDATE produkt set lagerbestand = lagerbestand - diff where sku = new.sku;
    return new;
end

$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bestellposition_update
    BEFORE UPDATE OF menge
    ON bestellposition
    FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_update();

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

-- ============================================================
-- views:
-- ============================================================

CREATE OR REPLACE VIEW v_kunde_summe_anzahl_bestellungen AS
SELECT k.kunde_id,
       k.email,
       COALESCE(count(b.bestellung_id), 0)  AS anzahl_bestellungen,
       COALESCE(sum(p.preis * bp.menge), 0) AS gesamtsumme
FROM kunde k
         LEFT JOIN bestellung b on k.kunde_id = b.kunde_id
         left join bestellposition bp on b.bestellung_id = bp.bestellung_id
         LEFT JOIN produkt p on bp.sku = p.sku
GROUP BY k.kunde_id, k.email
ORDER BY k.kunde_id ASC;

CREATE OR REPLACE VIEW v_produkt_verkaufszahlen AS
SELECT p.sku,
       p.name,
       COALESCE(sum(CASE WHEN b.status NOT IN ('neu', 'storniert') THEN bp.menge else 0 END),
                0)                                                                                         as gesamtverkaufteMenge,
       COALESCE(SUM(CASE WHEN b.status NOT IN ('neu', 'storniert') THEN bp.menge * p.preis ELSE 0 END), 0) as umsatz,
       COALESCE(COUNT(DISTINCT CASE WHEN b.status NOT IN ('neu', 'storniert') THEN b.bestellung_id END),
                0)                                                                                         as anzahlBestellungen
FROM produkt p
         LEFT JOIN bestellposition bp ON p.sku = bp.sku
         LEFT JOIN bestellung b ON bp.bestellung_id = b.bestellung_id
GROUP BY p.sku, p.name
ORDER BY gesamtverkaufteMenge DESC;

CREATE OR REPLACE VIEW v_mitarbeiter_uebersicht AS
SELECT m.personal_nr                      AS personalnr,
       COALESCE(b.anzahl_bestellungen, 0) AS anzahlverwalteterbestellungen,
       COALESCE(p.anzahl_produkte, 0)     AS anzahlangelegterprodukte
FROM mitarbeiter m
         LEFT JOIN (SELECT mitarbeiterzuweis, COUNT(DISTINCT bestellung_id) AS anzahl_bestellungen
                    FROM bestellung
                    GROUP BY mitarbeiterzuweis) b ON m.personal_nr = b.mitarbeiterzuweis
         LEFT JOIN (SELECT angelegt_von, COUNT(DISTINCT sku) AS anzahl_produkte
                    FROM produkt
                    GROUP BY angelegt_von) p ON m.personal_nr = p.angelegt_von
ORDER BY m.personal_nr ASC;

CREATE OR REPLACE VIEW v_mitarbeiter_bestellstatus_uebersicht AS
WITH status_list AS (SELECT unnest(array ['neu','bezahlt','versendet','storniert','abgeschlossen']) AS status)
SELECT m.personal_nr          AS personalNr,
       s.status,
       COUNT(b.bestellung_id) AS anzahlBestellungen
FROM mitarbeiter m
         CROSS JOIN
     status_list s
         LEFT JOIN
     bestellung b
     ON b.mitarbeiterzuweis = m.personal_nr AND b.status = s.status
GROUP BY m.personal_nr, s.status
ORDER BY m.personal_nr, s.status;
