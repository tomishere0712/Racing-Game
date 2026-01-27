# Racing Game - Android Mini Game

## 🏁 Project Overview
This is a racing mini-game where players can login, place bets on multiple cars, watch an animated race, and see their results with winnings!

## ✨ Features

### 1. **Login & Registration System**
- Hardcoded user authentication (no database required)
- Pre-configured test users
- Starting balance: $10,000 per user
- Persistent balance using SharedPreferences

### 2. **Betting System**
- Choose from 5 different colored cars
- Place bets on multiple cars simultaneously
- Real-time bet validation
- Balance tracking
- Minimum bet: $10 per car

### 3. **Racing Animation**
- Smooth car animations using ValueAnimator
- Random speed generation for each race
- 5 racing lanes with visual track design
- Real-time position updates
- Finish line detection

### 4. **Results & Winnings**
- Podium display (1st, 2nd, 3rd place)
- Detailed bet results
- 3x payout for winning bets
- Updated balance display
- Play again functionality

### 5. **Sound Effects**
- Login screen background music
- Betting screen ambience
- Racing sound effects
- Victory celebration sounds

## 📱 Screens (Layouts)

1. **LoginActivity** - Login/Register screen
2. **BettingActivity** - Place bets on cars
3. **RacingActivity** - Watch the race (landscape mode)
4. **ResultActivity** - View results and winnings

## 🎮 How to Play

1. **Login**: Use one of these test accounts or create a new one:
   - Username: `admin`, Password: `admin123`
   - Username: `player1`, Password: `pass123`
   - Username: `demo`, Password: `demo`

2. **Place Bets**: 
   - Check the cars you want to bet on
   - Enter bet amounts (minimum $10)
   - Total bets cannot exceed your balance

3. **Watch Race**: 
   - Tap "START RACE" button
   - Watch cars race to the finish line
   - First car to reach the finish wins!

4. **Check Results**:
   - See podium positions
   - View your bet outcomes
   - Winning bets pay 3x your wager
   - Play again or return to betting

## 🎨 Design Elements

### Cars (Vector Drawables)
- 🚗 Red Racer
- 🚙 Blue Lightning  
- 🚕 Green Machine
- 🚖 Yellow Thunder
- 🚐 Purple Storm

### Color Scheme
- Racing Blue (#2196F3)
- Racing Green (#4CAF50)
- Racing Red (#F44336)
- Gold (#FFD700)
- Dark (#212121)

### Backgrounds
- Login: Purple gradient
- Betting: Blue gradient
- Racing: Green gradient with lane markings
- Results: Teal gradient

## 🔧 Technical Details

### Architecture
- **Models**: User, Car, Bet
- **Utils**: UserManager (Singleton), SoundManager (Singleton)
- **Activities**: Login, Betting, Racing, Result

### Key Technologies
- ValueAnimator for smooth car movement
- SharedPreferences for data persistence
- MediaPlayer for sound management
- Custom vector drawables for cars
- Material Design components

### Animation Details
- Linear interpolation for consistent speed
- Random duration (3-7 seconds) per car
- Real-time position tracking
- Finish position detection

## 📦 Sound Files

Place these MP3 files in `app/src/main/res/raw/`:
- `login_sound.mp3` - Background music for login
- `betting_sound.mp3` - Background music for betting
- `racing_sound.mp3` - Racing engine sounds
- `victory_sound.mp3` - Victory celebration

**Note**: The app will work without sound files (gracefully handles missing audio).

## 🚀 Building & Running

1. Open project in Android Studio
2. Sync Gradle files
3. Connect Android device or start emulator
4. Run the app (Shift + F10)

### Requirements
- Android Studio Hedgehog or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 36
- Compile SDK: 36

## 📝 Code Structure

```
app/src/main/java/com/example/demogame/
├── models/
│   ├── User.java
│   ├── Car.java
│   └── Bet.java
├── utils/
│   ├── UserManager.java
│   └── SoundManager.java
├── LoginActivity.java
├── BettingActivity.java
├── RacingActivity.java
└── ResultActivity.java

app/src/main/res/
├── layout/
│   ├── activity_login.xml
│   ├── activity_betting.xml
│   ├── activity_racing.xml
│   ├── activity_result.xml
│   └── item_car_bet.xml
├── drawable/
│   ├── car_red.xml
│   ├── car_blue.xml
│   ├── car_green.xml
│   ├── car_yellow.xml
│   ├── car_purple.xml
│   ├── bg_*.xml (backgrounds)
│   └── ic_racing_logo.xml
└── values/
    ├── colors.xml
    ├── strings.xml
    └── themes.xml
```

## 🎯 Game Rules

- **Starting Balance**: $10,000
- **Minimum Bet**: $10 per car
- **Maximum Bets**: Limited by your balance
- **Payout**: 3x your bet for winning car
- **Multiple Bets**: You can bet on multiple cars in same race
- **Random Racing**: Each race has different outcomes

## 💡 Tips

- Spread your bets across multiple cars to increase winning chances
- Watch for patterns (just kidding - it's completely random!)
- Manage your balance wisely
- Have fun!

## 🐛 Troubleshooting

### Sound Issues
- If sounds don't play, check that MP3 files are in `res/raw/`
- The app will continue to work without sound

### Build Errors
- Clean and rebuild project: Build > Clean Project, then Build > Rebuild Project
- Invalidate caches: File > Invalidate Caches / Restart

### Screen Orientation
- Racing activity is landscape mode
- Other screens are portrait mode
- If orientation doesn't change, check device auto-rotate settings

## 📄 License

This is an educational project for learning Android development.

## 👨‍💻 Developer Notes

This mini-game demonstrates:
- Multi-activity Android app structure
- Custom animations with ValueAnimator
- Singleton pattern for managers
- SharedPreferences for data persistence
- Vector drawable creation
- Material Design implementation
- Sound management
- State management across activities

Enjoy the race! 🏁🏎️
