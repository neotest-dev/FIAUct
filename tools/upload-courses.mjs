import fs from "node:fs";
import path from "node:path";
import process from "node:process";
import admin from "firebase-admin";

const rootDir = path.resolve(process.cwd());
const serviceAccountPath = process.env.GOOGLE_APPLICATION_CREDENTIALS;
const coursesPath = path.join(rootDir, "app", "src", "main", "assets", "courses.json");

if (!serviceAccountPath) {
  throw new Error("Set GOOGLE_APPLICATION_CREDENTIALS with your service account JSON path.");
}

const serviceAccount = JSON.parse(fs.readFileSync(serviceAccountPath, "utf8"));
const courses = JSON.parse(fs.readFileSync(coursesPath, "utf8"));

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();
const collection = db.collection("courses");

const chunks = [];
for (let i = 0; i < courses.length; i += 450) {
  chunks.push(courses.slice(i, i + 450));
}

let uploaded = 0;
for (const chunk of chunks) {
  const batch = db.batch();
  for (const course of chunk) {
    if (!course.codigo) {
      continue;
    }
    const ref = collection.doc(course.codigo);
    batch.set(ref, {
      programa: course.programa ?? "",
      modalidad: course.modalidad ?? "",
      ciclo: course.ciclo ?? "",
      codigo: course.codigo,
      curso: course.curso ?? "",
      docente: course.docente ?? "",
      "mod-curso": course["mod-curso"] ?? course.modCurso ?? null,
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    });
    uploaded += 1;
  }
  await batch.commit();
}

console.log(`Uploaded ${uploaded} courses to Firestore collection "courses".`);
