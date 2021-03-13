CREATE TABLE his_export
(
    mtknr        VARCHAR(255),
    zim_kennung  VARCHAR(32) NOT NULL PRIMARY KEY,
    bibkz        VARCHAR(32),
    geschl       VARCHAR(32),
    nachname     VARCHAR(32),
    vorname      VARCHAR(32),
    gebdat       VARCHAR(32),
    strasse      VARCHAR(32),
    pozusatz     VARCHAR(32),
    plz          VARCHAR(32),
    ort          VARCHAR(32),
    land         VARCHAR(32),
    festnetz     VARCHAR(32),
    mobil        VARCHAR(32),
    email        VARCHAR(32),
    immadatum    VARCHAR(32),
    status       VARCHAR(32),
    semester     VARCHAR(32),
    campus       VARCHAR(32),
    abschluss1   VARCHAR(32),
    fach1        VARCHAR(32),
    fach2        VARCHAR(32),
    fach3        VARCHAR(32),
    cardCurrens  VARCHAR(32),
    hoererStatus VARCHAR(32),
    exgr         VARCHAR(32),
    exmagrund    VARCHAR(32),
    exmadatum    VARCHAR(32),
    exmaantrag   VARCHAR(32)
);