-- Script di inizializzazione per il database di valutazione e raccomandazione di libri
-- DROP DATABASE IF EXISTS book;
-- CREATE DATABASE book;

-- Creazione della tabella UtentiRegistrati
CREATE TABLE UtentiRegistrati (
    userid SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    codice_fiscale VARCHAR(16) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Creazione della tabella Librerie
CREATE TABLE Librerie (
    id SERIAL PRIMARY KEY,
    nome_libreria VARCHAR(100) NOT NULL,
    userid INT NOT NULL,
    FOREIGN KEY (userid) REFERENCES UtentiRegistrati(userid) ON DELETE CASCADE
);

-- Creazione della tabella Libri
CREATE TABLE Libri (
    id SERIAL PRIMARY KEY,
    titolo VARCHAR(255) NOT NULL,
    autore VARCHAR(255) NOT NULL,
    editore VARCHAR(255),
    categoria TEXT[],
    anno INT CHECK (anno > 0)
);

-- Creazione della tabella ValutazioniLibri
CREATE TABLE ValutazioniLibri (
    id SERIAL PRIMARY KEY,
    libro_id INT NOT NULL,
    utente_id INT NOT NULL,
    stile INT CHECK (stile BETWEEN 1 AND 5),
    contenuto INT CHECK (contenuto BETWEEN 1 AND 5),
    gradevolezza INT CHECK (gradevolezza BETWEEN 1 AND 5),
    originalita INT CHECK (originalita BETWEEN 1 AND 5),
    edizione INT CHECK (edizione BETWEEN 1 AND 5),
    voto_finale INT CHECK (voto_finale BETWEEN 1 AND 5),
    note VARCHAR(256),
    FOREIGN KEY (libro_id) REFERENCES Libri(id) ON DELETE CASCADE,
    FOREIGN KEY (utente_id) REFERENCES UtentiRegistrati(userid) ON DELETE CASCADE
);

-- Creazione della tabella ConsigliLibri
CREATE TABLE ConsigliLibri (
    id SERIAL PRIMARY KEY,
    libro_id INT NOT NULL,
    utente_id INT NOT NULL,
    libro_suggerito_id INT NOT NULL,
    FOREIGN KEY (libro_id) REFERENCES Libri(id) ON DELETE CASCADE,
    FOREIGN KEY (utente_id) REFERENCES UtentiRegistrati(userid) ON DELETE CASCADE,
    FOREIGN KEY (libro_suggerito_id) REFERENCES Libri(id) ON DELETE CASCADE
);

-- Creazione della tabella per memorizzare le valutazioni aggregate (opzionale)
CREATE TABLE ValutazioniAggregate (
    libro_id INT NOT NULL,
    numero_utenti INT DEFAULT 0,
    media_stile DECIMAL(3,2) DEFAULT 0,
    media_contenuto DECIMAL(3,2) DEFAULT 0,
    media_gradevolezza DECIMAL(3,2) DEFAULT 0,
    media_originalita DECIMAL(3,2) DEFAULT 0,
    media_edizione DECIMAL(3,2) DEFAULT 0,
    FOREIGN KEY (libro_id) REFERENCES Libri(id) ON DELETE CASCADE
);

-- Indici per migliorare le performance delle query
CREATE INDEX idx_libro_autore ON Libri(autore);
CREATE INDEX idx_valutazioni_libro ON ValutazioniLibri(libro_id);
CREATE INDEX idx_consigli_libro ON ConsigliLibri(libro_id);