# Sound Files Instructions

This project requires the following sound files to be placed in the `app/src/main/res/raw/` directory:

1. **login_sound.mp3** - Background music for login screen (calm, welcoming music)
2. **betting_sound.mp3** - Background music for betting screen (exciting, anticipatory)
3. **racing_sound.mp3** - Sound effect during the race (engine sounds, racing atmosphere)
4. **victory_sound.mp3** - Victory fanfare for results screen (celebratory)

## Where to find free sound files:

- **Freesound.org** - https://freesound.org/
- **Zapsplat** - https://www.zapsplat.com/
- **Free Music Archive** - https://freemusicarchive.org/

## Temporary Workaround:

For testing without sound files, you can:
1. Comment out the SoundManager calls in each Activity
2. Or create silent/dummy MP3 files

## How to add sound files:

1. Download MP3 files
2. Rename them to: login_sound.mp3, betting_sound.mp3, racing_sound.mp3, victory_sound.mp3
3. Place them in: app/src/main/res/raw/
4. Rebuild the project
