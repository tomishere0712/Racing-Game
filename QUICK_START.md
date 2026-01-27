# 🎮 Quick Start Guide - Racing Game

## 🚀 Getting Started

### 1. Open the Project
- Open Android Studio
- Select "Open an Existing Project"
- Navigate to: `/Users/phucnguyenvinh/AndroidStudioProjects/DemoGame`
- Click "OK"

### 2. Wait for Sync
- Android Studio will automatically sync Gradle
- Wait for "Gradle Build Finished" message

### 3. Run the App
- Connect an Android device (USB debugging enabled) OR start an emulator
- Click the green "Run" button (or press Shift + F10)
- Select your device
- Wait for app to install and launch

## 📱 Testing the Game

### Test Account Credentials
```
Username: admin
Password: admin123

Username: player1
Password: pass123

Username: demo
Password: demo
```

### Test Scenario 1: Quick Play
1. Login with `demo` / `demo`
2. Select "Red Racer" - bet $100
3. Select "Blue Lightning" - bet $100
4. Tap "START RACE"
5. Watch the race!
6. View results

### Test Scenario 2: Multiple Bets
1. Login with `admin` / `admin123`
2. Check all 5 cars
3. Bet $50 on each car (total $250)
4. Start race
5. See which car wins!

### Test Scenario 3: High Stakes
1. Login with `player1` / `pass123`
2. Bet $1000 on one car
3. If you win: $3000 payout!
4. If you lose: Try again with remaining balance

## 🎯 Game Rules Reminder

- **Starting Balance**: $10,000
- **Minimum Bet**: $10 per car
- **Win Multiplier**: 3x your bet
- **Multiple Bets**: Yes, bet on multiple cars!
- **Balance Persistence**: Your balance is saved

## 🔊 Sound Files (Optional)

The game works without sound, but for better experience:

1. Download 4 MP3 files (login, betting, racing, victory sounds)
2. Rename them to:
   - `login_sound.mp3`
   - `betting_sound.mp3`
   - `racing_sound.mp3`
   - `victory_sound.mp3`
3. Place in: `app/src/main/res/raw/`
4. Rebuild project

## 🐛 Common Issues

### Issue: App crashes on sound playback
**Solution**: The placeholder sound files are empty. Either:
- Add real MP3 files (see above)
- Or the app handles this gracefully - just continue playing

### Issue: Racing screen stays in portrait
**Solution**: 
- Check your device's auto-rotate settings
- The RacingActivity is set to landscape in AndroidManifest

### Issue: Balance doesn't save
**Solution**:
- Balance is saved using SharedPreferences
- Make sure you're logging in with the same username
- Balance persists across app restarts

### Issue: Can't place bet
**Possible reasons**:
- Bet amount is less than $10
- Total bets exceed your balance
- You didn't check any car checkbox

## 📊 Project Structure

```
DemoGame/
├── Models (User, Car, Bet)
├── Utils (UserManager, SoundManager)
├── Activities
│   ├── LoginActivity (Portrait)
│   ├── BettingActivity (Portrait)
│   ├── RacingActivity (Landscape)
│   └── ResultActivity (Portrait)
├── Layouts (5 XML files)
├── Drawables (Car vectors, backgrounds)
└── Resources (colors, strings)
```

## 🎨 Customization Ideas

Want to modify the game?

### Change Starting Balance
- File: `UserManager.java`
- Line: `private static final double INITIAL_BALANCE = 10000.0;`
- Change to your desired amount

### Change Win Multiplier
- File: `ResultActivity.java`
- Line: `totalWinnings = betAmount * 3;`
- Change `3` to your desired multiplier

### Change Minimum Bet
- File: `BettingActivity.java`
- Line: `if (amount < 10)`
- Change `10` to your desired minimum

### Add More Cars
- Create new car drawable in `res/drawable/`
- Add to `initializeCars()` in BettingActivity and RacingActivity
- Update layouts to accommodate more lanes

### Change Race Duration
- File: `RacingActivity.java`
- Line: `int baseDuration = 3000 + random.nextInt(4000);`
- Adjust values (3000ms = 3 seconds base)

## 🏆 Tips for Best Experience

1. **Start with small bets** to learn the game
2. **Try different strategies** - bet on one car or spread bets
3. **Watch your balance** - don't bet everything at once!
4. **Portrait for betting, landscape for racing** - optimal viewing
5. **Each race is random** - pure luck based!

## 📸 Screenshots Locations
_(If you take screenshots, they'll be useful for documentation)_

Recommended screenshots:
1. Login screen
2. Betting screen with selections
3. Racing screen mid-race
4. Results screen showing win
5. Results screen showing loss

## 🔧 Advanced: Gradle Commands

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run all tests
./gradlew test
```

## 📝 Next Steps

After testing:
1. ✅ Try all test accounts
2. ✅ Test betting validation
3. ✅ Watch complete race
4. ✅ Verify balance updates
5. ✅ Test multiple sessions
6. ✅ Add your own sound files
7. ✅ Customize colors/values
8. ✅ Share with friends!

---

**Need Help?**
- Check `README.md` for detailed documentation
- Review code comments in Java files
- Check `SOUND_FILES_README.md` for audio setup

**Enjoy the race!** 🏁🏎️💨
