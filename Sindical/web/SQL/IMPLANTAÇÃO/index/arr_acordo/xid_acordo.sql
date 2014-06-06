
-- Index: xid_acordo

-- DROP INDEX xid_acordo;

CREATE INDEX xid_acordo
  ON arr_acordo
  USING btree
  (id);
