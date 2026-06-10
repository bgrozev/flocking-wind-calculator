---
name: add-dropzone
description: Add a new dropzone to FWC's Dropzones.kt list. Use when user asks to add a dropzone, given a name.
---

# Add Dropzone

Steps:

1. Find lat/lon (3 decimals) of the dropzone (web search / known location).
2. Open `src/main/kotlin/Dropzones.kt`. List is alphabetically sorted by name (`Dropzone("Name", lat, lon),`). Insert new entry in correct alphabetical spot.
3. Show user a Google Maps link: `https://www.google.com/maps?q=<lat>,<lon>` and ask them to confirm location is correct.
4. After confirmation, edit file with the new entry.
5. Commit: `git add src/main/kotlin/Dropzones.kt && git commit -m "Add <Name>."` (match style of past commits, e.g. "Add Skydive Carolina.").
