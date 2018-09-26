
    CREATE TABLE IF NOT EXISTS Company (
          id SERIAL,
          name VARCHAR(255),
          nip VARCHAR(255),
          street VARCHAR(255),
          postal_code VARCHAR(255),
          city VARCHAR(255),
          discount NUMERIC NOT NULL,
          PRIMARY KEY ( id ));

    CREATE TABLE IF NOT EXISTS Invoice (
          Id SERIAL,
          Identifier VARCHAR(255) NOT NULL,
          issue_date DATE NOT NULL,
          sale_date DATE NOT NULL,
          sale_place VARCHAR(255) NOT NULL,
          buyer_id INTEGER NOT NULL REFERENCES Company(id),
          seller_id INTEGER NOT NULL REFERENCES Company(id),
          PRIMARY KEY(Id));

    CREATE TABLE IF NOT EXISTS invoice_entry (
          Id SERIAL,
          description VARCHAR(255) NOT NULL,
          net_price NUMERIC NOT NULL,
          vat_rate NUMERIC NOT NULL,
          quantity NUMERIC NOT NULL,
          PRIMARY KEY(Id));

    CREATE TABLE IF NOT EXISTS invoice_entries (
          invoice_id INTEGER NOT NULL REFERENCES Invoice(Id),
          entries_id INTEGER NOT NULL REFERENCES invoice_entry(Id));