-- Script di inizializzazione per il database di valutazione e raccomandazione di libri
-- DROP DATABASE IF EXISTS book;
-- CREATE DATABASE book;

-- Creazione della tabella UtentiRegistrati
CREATE TABLE UtentiRegistrati (
    id SERIAL PRIMARY KEY,
    userid VARCHAR(100) UNIQUE NOT NULL,
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
    libri INT[],
    FOREIGN KEY (userid) REFERENCES UtentiRegistrati(id) ON DELETE CASCADE
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
    FOREIGN KEY (utente_id) REFERENCES UtentiRegistrati(id) ON DELETE CASCADE
);

-- Creazione della tabella ConsigliLibri
CREATE TABLE ConsigliLibri (
    id SERIAL PRIMARY KEY,
    libro_id INT NOT NULL,
    utente_id INT NOT NULL,
    libro1_suggerito_id INT NOT NULL,
    libro2_suggerito_id INT NOT NULL,
    libro3_suggerito_id INT NOT NULL,
    FOREIGN KEY (libro_id) REFERENCES Libri(id) ON DELETE CASCADE,
    FOREIGN KEY (utente_id) REFERENCES UtentiRegistrati(id) ON DELETE CASCADE,
    FOREIGN KEY (libro1_suggerito_id) REFERENCES Libri(id) ON DELETE CASCADE,
    FOREIGN KEY (libro2_suggerito_id) REFERENCES Libri(id) ON DELETE CASCADE,
    FOREIGN KEY (libro3_suggerito_id) REFERENCES Libri(id) ON DELETE CASCADE
);

-- Indici per migliorare le performance delle query
CREATE INDEX idx_libro_autore ON Libri(autore);
CREATE INDEX idx_valutazioni_libro ON ValutazioniLibri(libro_id);
CREATE INDEX idx_consigli_libro ON ConsigliLibri(libro_id);

INSERT INTO UtentiRegistrati 
(userid, nome, cognome, codice_fiscale, email, password)
VALUES 
('matia', 'mattia', 'papaccioli', 'ppp', 'ppp', '1234');

INSERT INTO Librerie 
(nome_libreria, userid, libri)
VALUES
('test1', 1, ARRAY[1, 2, 3]),
('test2', 1, ARRAY[4, 5, 7]);

INSERT INTO Libri 
(id, titolo, autore, categoria, anno)
VALUES
(0, 'null', 'null', '{"null", "null"}',  9999);