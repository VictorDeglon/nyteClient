// Image prep for the landing page (docs/), using sharp.
//   1. Writes docs/assets/og-image.png (1200x630 social card, Theme.java palette).
//   2. Converts any PNG/JPG dropped in docs/assets/screenshots/ to a max-1600px-wide
//      WebP next to it, so the page can reference small files.
// Run: cd tools && npm install && npm run images
import { readdir } from 'node:fs/promises';
import { dirname, extname, join, parse } from 'node:path';
import { fileURLToPath } from 'node:url';
import sharp from 'sharp';

const docs = join(dirname(fileURLToPath(import.meta.url)), '..', 'docs');
const shots = join(docs, 'assets', 'screenshots');

const og = `
<svg xmlns="http://www.w3.org/2000/svg" width="1200" height="630">
  <rect width="1200" height="630" fill="#0d0b12"/>
  <rect x="60" y="60" width="1080" height="510" rx="28" fill="#191622" stroke="#ffffff1a" stroke-width="2"/>
  <rect x="60" y="60" width="1080" height="96" rx="28" fill="#241f30"/>
  <circle cx="108" cy="108" r="14" fill="#8a5cff"/>
  <text x="140" y="120" font-family="Helvetica, Arial, sans-serif" font-size="34" font-weight="700" fill="#f2f0fa">Nyte Client</text>
  <text x="120" y="320" font-family="Helvetica, Arial, sans-serif" font-size="92" font-weight="800" fill="#f2f0fa">Fabric mod for</text>
  <text x="120" y="425" font-family="Helvetica, Arial, sans-serif" font-size="92" font-weight="800" fill="#8a5cff">Minecraft</text>
  <text x="120" y="500" font-family="Helvetica, Arial, sans-serif" font-size="36" fill="#9d97b5">Toggleable performance and QoL modules. Free download.</text>
</svg>`;

await sharp(Buffer.from(og)).png({ compressionLevel: 9 }).toFile(join(docs, 'assets', 'og-image.png'));
console.log('wrote docs/assets/og-image.png');

for (const name of await readdir(shots)) {
  const { name: base } = parse(name);
  if (!['.png', '.jpg', '.jpeg'].includes(extname(name).toLowerCase())) continue;
  const out = join(shots, `${base}.webp`);
  await sharp(join(shots, name)).resize({ width: 1600, withoutEnlargement: true }).webp({ quality: 82 }).toFile(out);
  console.log(`wrote docs/assets/screenshots/${base}.webp`);
}
