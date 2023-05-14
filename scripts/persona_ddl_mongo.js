use admin

db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "read", db: "prueba_db" },
    { role: "readWrite", db: "prueba_db" },
    { role: "dbAdmin", db: "prueba_db" }
  ],
  mechanisms: ["SCRAM-SHA-1","SCRAM-SHA-256"]
})
