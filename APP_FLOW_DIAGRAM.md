# 🎮 Racing Game - App Flow Diagram

## 📱 Application Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                         APP LAUNCH                               │
│                             ↓                                    │
│  ┌──────────────────────────────────────────────────────┐      │
│  │         LOGIN ACTIVITY (Portrait)                     │      │
│  │  ┌────────────────────────────────────────────────┐  │      │
│  │  │  🏁 Racing Game Logo                          │  │      │
│  │  │                                                │  │      │
│  │  │  Username: [____________]                     │  │      │
│  │  │  Password: [____________]                     │  │      │
│  │  │                                                │  │      │
│  │  │  [       LOGIN       ]                        │  │      │
│  │  │  [      REGISTER     ]                        │  │      │
│  │  │                                                │  │      │
│  │  │  Test accounts: admin/admin123                │  │      │
│  │  │                 demo/demo                     │  │      │
│  │  └────────────────────────────────────────────────┘  │      │
│  └──────────────────────────────────────────────────────┘      │
│                             ↓                                    │
│                      [Valid Login]                               │
│                             ↓                                    │
│  ┌──────────────────────────────────────────────────────┐      │
│  │        BETTING ACTIVITY (Portrait)                    │      │
│  │  ┌────────────────────────────────────────────────┐  │      │
│  │  │  Player: admin    Balance: $10,000  [Logout]  │  │      │
│  │  │  Total Bet: $0.00                              │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │  🏁 Place Your Bets 🏁                        │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │  [✓] 🚗 Red Racer      [_100_]               │  │      │
│  │  │  [ ] 🚙 Blue Lightning  [____]               │  │      │
│  │  │  [✓] 🚕 Green Machine   [_50__]              │  │      │
│  │  │  [ ] 🚖 Yellow Thunder  [____]               │  │      │
│  │  │  [ ] 🚐 Purple Storm    [____]               │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │         [ 🏁 START RACE 🏁 ]                  │  │      │
│  │  └────────────────────────────────────────────────┘  │      │
│  └──────────────────────────────────────────────────────┘      │
│                             ↓                                    │
│                      [Place Bets & Start]                        │
│                             ↓                                    │
│  ┌──────────────────────────────────────────────────────┐      │
│  │       RACING ACTIVITY (Landscape) 📱→                 │      │
│  │  ┌────────────────────────────────────────────────┐  │      │
│  │  │  Race in Progress...                           │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │  🚗─────────────────────────────────────┃     │  │      │
│  │  │  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓┃     │  │      │
│  │  │                                                 │  │      │
│  │  │  🚙─────────────────────────────────────────┃  │  │      │
│  │  │  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓┃     │  │      │
│  │  │                                                 │  │      │
│  │  │       🚕──────────────────────────────────┃     │  │      │
│  │  │  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓┃     │  │      │
│  │  │                                                 │  │      │
│  │  │  🚖─────────────────────────────────────┃      │  │      │
│  │  │  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓┃     │  │      │
│  │  │                                                 │  │      │
│  │  │  🚐─────────────────────────────────────────┃  │  │      │
│  │  │  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓┃     │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │     [  START  ]         [  RESET  ]            │  │      │
│  │  └────────────────────────────────────────────────┘  │      │
│  └──────────────────────────────────────────────────────┘      │
│                             ↓                                    │
│                      [Race Finishes]                             │
│                             ↓                                    │
│  ┌──────────────────────────────────────────────────────┐      │
│  │        RESULT ACTIVITY (Portrait)                     │      │
│  │  ┌────────────────────────────────────────────────┐  │      │
│  │  │  🎉 Congratulations! You Won! 🎉               │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │              PODIUM                             │  │      │
│  │  │                                                 │  │      │
│  │  │     🥈          🏆          🥉                 │  │      │
│  │  │   Blue       Red Racer    Green                │  │      │
│  │  │  Lightning    WINNER     Machine               │  │      │
│  │  │  ┌────┐     ┌──────┐    ┌────┐               │  │      │
│  │  │  │ 2nd│     │ 1st  │    │ 3rd│               │  │      │
│  │  │  └────┘     └──────┘    └────┘               │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │  Your Betting Results:                         │  │      │
│  │  │  ✅ Red Racer   - Bet: $100 - Won: $300      │  │      │
│  │  │  ❌ Green Machine - Lost: $50                 │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │  Total Winnings: $300.00                       │  │      │
│  │  │  New Balance: $10,150.00                       │  │      │
│  │  ├────────────────────────────────────────────────┤  │      │
│  │  │        [ 🎮 PLAY AGAIN 🎮 ]                   │  │      │
│  │  │        [ ↩️ Back to Betting ]                  │  │      │
│  │  └────────────────────────────────────────────────┘  │      │
│  └──────────────────────────────────────────────────────┘      │
│                             ↓                                    │
│                      [Play Again] → Back to Betting              │
│                      [Back] → Back to Betting                    │
│                      [Logout] → Back to Login                    │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 User Journey Scenarios

### Scenario 1: First Time User
```
1. App Launch → LoginActivity
2. Click "Register" button
3. Enter username: "newplayer"
4. Enter password: "pass123"
5. Click "Create Account"
   → UserManager creates new user with $10,000
   → Navigate to BettingActivity
6. See welcome with $10,000 balance
```

### Scenario 2: Returning User
```
1. App Launch → LoginActivity
2. Enter username: "admin"
3. Enter password: "admin123"
4. Click "Login"
   → UserManager validates credentials
   → Load saved balance from SharedPreferences
   → Navigate to BettingActivity
5. See current balance (may be ≠ $10,000 if played before)
```

### Scenario 3: Place Single Bet
```
1. BettingActivity loaded
2. Check "Red Racer" checkbox
3. Enter amount: "500"
4. Total Bet shows: $500.00
5. Click "START RACE"
   → Validate: amount ≥ $10 ✓
   → Validate: total ≤ balance ✓
   → Deduct $500 from balance
   → Navigate to RacingActivity with bet data
```

### Scenario 4: Place Multiple Bets
```
1. BettingActivity loaded (Balance: $10,000)
2. Check "Red Racer" → Enter: $200
3. Check "Blue Lightning" → Enter: $150
4. Check "Yellow Thunder" → Enter: $100
5. Total Bet shows: $450.00
6. Click "START RACE"
   → All validations pass
   → Deduct $450 from balance
   → Navigate to RacingActivity
```

### Scenario 5: Win Race
```
1. RacingActivity starts
2. Click "START" button
3. Random speeds assigned:
   - Red: 5000ms
   - Blue: 4500ms ← Fastest!
   - Green: 5500ms
   - Yellow: 6000ms
   - Purple: 5800ms
4. Cars animate to finish line
5. Blue finishes first
6. All cars finish
7. Navigate to ResultActivity
8. Calculate results:
   - Bet on Blue: $150 → Win: $450 (3x)
   - Bet on Red: $200 → Lost
9. Add winnings: $10,000 - $350 + $450 = $10,100
10. Display results with podium
```

### Scenario 6: Lose Race
```
1. BettingActivity: Bet $100 on Red
2. RacingActivity: Yellow wins (Red loses)
3. ResultActivity:
   - Lost: $100
   - Winnings: $0
   - New Balance: $9,900
4. Click "PLAY AGAIN"
5. Return to BettingActivity with updated balance
```

### Scenario 7: Error - Insufficient Balance
```
1. BettingActivity (Balance: $50)
2. Check "Red Racer" → Enter: $100
3. Click "START RACE"
   → Validation fails: $100 > $50
   → Toast: "Insufficient balance!"
   → Stay on BettingActivity
```

### Scenario 8: Error - Minimum Bet
```
1. BettingActivity
2. Check "Red Racer" → Enter: $5
3. Click "START RACE"
   → Validation fails: $5 < $10
   → Toast: "Minimum bet per car is $10"
   → Stay on BettingActivity
```

## 🎯 Key Interaction Points

### Login Screen
- **Input**: Username, Password
- **Actions**: Login, Register, Switch Mode
- **Validation**: Non-empty fields, min lengths
- **Output**: Navigate to Betting or show error

### Betting Screen
- **Input**: Car selection (checkboxes), Bet amounts
- **Actions**: Select cars, Enter amounts, Start Race, Logout
- **Validation**: Min $10, Total ≤ Balance, At least one bet
- **Output**: Navigate to Racing or show error

### Racing Screen  
- **Input**: Start button press
- **Actions**: Start race, Reset race, Back (blocked during race)
- **Animation**: ValueAnimator with random speeds
- **Output**: Navigate to Results when race finishes

### Results Screen
- **Input**: None (display only)
- **Actions**: Play Again, Back to Betting
- **Calculation**: Winnings = (bet × 3) if winner, else 0
- **Output**: Navigate to Betting with updated balance

## 📊 Data Flow

```
┌─────────────┐
│  Login      │
│  Activity   │
└──────┬──────┘
       │ username, password
       ↓
┌─────────────┐         ┌──────────────┐
│   User      │←────────│ Shared       │
│   Manager   │────────→│ Preferences  │
└──────┬──────┘  save   └──────────────┘
       │ User object      load
       ↓
┌─────────────┐
│  Betting    │
│  Activity   │
└──────┬──────┘
       │ bets (HashMap)
       ↓
┌─────────────┐
│  Racing     │
│  Activity   │
└──────┬──────┘
       │ car results (ArrayList)
       ↓
┌─────────────┐
│  Result     │
│  Activity   │
└──────┬──────┘
       │ updated balance
       ↓
┌─────────────┐         ┌──────────────┐
│   User      │────────→│ Shared       │
│   Manager   │  save   │ Preferences  │
└─────────────┘         └──────────────┘
```

## 🎨 Screen Orientations

```
┌──────────────────────┐
│   LoginActivity      │  Portrait
│   (Portrait only)    │  📱
└──────────────────────┘

┌──────────────────────┐
│   BettingActivity    │  Portrait
│   (Portrait only)    │  📱
└──────────────────────┘

┌─────────────────────────────────┐
│   RacingActivity                │  Landscape
│   (Landscape only)              │  📱→
└─────────────────────────────────┘

┌──────────────────────┐
│   ResultActivity     │  Portrait
│   (Portrait only)    │  📱
└──────────────────────┘
```

## 🔊 Sound Timeline

```
Login Screen:    login_sound.mp3 (looping)
                 ↓
Betting Screen:  betting_sound.mp3 (looping)
                 ↓
Racing Screen:   racing_sound.mp3 (looping)
                 ↓
Results Screen:  victory_sound.mp3 (one-shot)
                 ↓
Back to Betting: betting_sound.mp3 (looping)
```

## 💾 State Management

### UserManager (Singleton)
```
- currentUser: User
- users: Map<String, User>
- Methods:
  - login(username, password)
  - register(username, password)
  - getCurrentUser()
  - saveUserBalance()
  - loadUserBalance()
```

### Data Passed Between Activities
```
Login → Betting:
  - User session (via UserManager)

Betting → Racing:
  - bets: HashMap<Integer, Double>
  - Intent.putExtra("bets", bets)

Racing → Result:
  - carNames: ArrayList<String>
  - carDrawables: ArrayList<Integer>
  - finishPositions: ArrayList<Integer>
  - bets: HashMap<Integer, Double>
```

---

**This diagram shows the complete user journey through the Racing Game app!** 🏁🏎️

For detailed code implementation, see the respective Java files in the project.
