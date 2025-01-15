BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "tb_account" (
	"id"	INTEGER,
	"account"	TEXT NOT NULL,
	"value"	REAL NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "tb_card" (
	"id"	INTEGER,
	"value"	REAL NOT NULL,
	"date"	DATE NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "tb_history" (
	"id"	INTEGER,
	"data_list"	TEXT NOT NULL,
	"Date_save"	DATE NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
COMMIT;

CREATE TABLE "tb_installments" (
	"id"	INTEGER,
	"item"	TEXT NOT NULL,
	"total_value"	REAL NOT NULL,
	"number_installment"	INTEGER NOT NULL,
	"installment_paid"	INTEGER,
	"last_payment"	DATE,
	PRIMARY KEY("id" AUTOINCREMENT)
);