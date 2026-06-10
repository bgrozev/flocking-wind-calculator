---
name: deploy-gh-pages
description: Build FWC and deploy build/distributions output to gh-pages branch. Use when user asks to deploy / publish to gh-pages.
---

# Deploy to gh-pages

Steps:

1. On `master`, run `./gradlew build`. Output goes to `build/distributions/` (index.html, styles.css, flocking-wind-drift.js, .js.map, .LICENSE.txt).
2. Note current `master` commit hash.
3. Checkout `gh-pages` branch (don't lose master changes - stash if needed, but master should be clean already since we only built).
4. Copy/overwrite the build output files into repo root on gh-pages: `index.html`, `styles.css`, `CNAME` (keep existing CNAME, don't overwrite from build unless build produces one), `flocking-wind-drift.js`, `flocking-wind-drift.js.map`, `flocking-wind-drift.js.LICENSE.txt`.
5. `git add` changed files, commit with message `Update to <master commit hash>.` (match existing gh-pages commit style).
6. Checkout back to `master`.
7. Push both branches: `git push origin master gh-pages`.
