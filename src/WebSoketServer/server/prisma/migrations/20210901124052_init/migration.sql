/*
  Warnings:

  - You are about to drop the column `guy` on the `groups` table. All the data in the column will be lost.

*/
-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_groups" (
    "name" TEXT NOT NULL PRIMARY KEY
);
INSERT INTO "new_groups" ("name") SELECT "name" FROM "groups";
DROP TABLE "groups";
ALTER TABLE "new_groups" RENAME TO "groups";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
