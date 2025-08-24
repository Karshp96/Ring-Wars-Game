# Ring Wars

A strategic twist on the classic Tic-Tac-Toe game where players use different sized rings to create winning combinations.

## Game Overview

Ring Wars is a 2-player strategy game played on a 3x3 grid. Each player has 9 rings in total: 3 small, 3 medium, and 3 large rings. The objective is to achieve one of three winning conditions before your opponent.

## Winning Conditions

1. **Same Size Line**: Get 3 rings of the same size in a row, column, or diagonal
2. **Size Progression**: Create a sequence of Small→Medium→Large (or reverse) in any line
3. **Concentric Stack**: Stack all 3 different sized rings in the same cell

## Game Rules

- Players take turns placing rings on the 3x3 grid
- Each player has 3 small, 3 medium, and 3 large rings in their color
- Rings can be placed on top of different sized rings in the same cell
- Same size rings cannot occupy the same position
- Once placed, rings cannot be moved
- If no valid moves remain, the player skips their turn

## Technology Stack

### Frontend
- **HTML** with modern CSS styling
- **Vanilla JavaScript** for game logic and UI interactions
- **CSS Grid & Flexbox** for responsive layout
- **CSS Animations** for enhanced visual effects

### Backend
- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Web** for REST API
- **Maven** for dependency management

## Project Structure

```
ring-wars/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── unitbase/
│                   └── game/
│                       ├── TicTacToeApplication.java
│                       ├── config/
│                       │   └── WebConfig.java
│                       ├── controller/
│                       │   ├── GameController.java
│                       │   └── intf/
│                       │       └── IGameController.java
│                       ├── model/
│                       │   ├── Cell.java
│                       │   ├── GameState.java
│                       │   ├── Player.java
│                       │   └── Ring.java
│                       └── service/
│                           └── GameService.java
├── ui
│   └── enhanced_ring_wars.html
├── pom.xml
└── README.md
```

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Modern web browser** (Chrome, Firefox, Safari, Edge)

## Installation & Setup

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ring-wars
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the Spring Boot application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or alternatively:
   ```bash
   java -jar target/tictactoe-0.0.1-SNAPSHOT.jar
   ```

4. **Verify backend is running**
   - Server starts on `http://localhost:8080`
   - Test endpoint: `http://localhost:8080/api/game/test`

### Frontend Setup

1. **Open the game**
   - Simply open `enhanced_ring_wars.html` in your web browser
   - Or serve it through a local HTTP server for development

2. **For local development server (optional)**
   ```bash
   # Using Python 3
   python -m http.server 3000
   
   # Using Node.js (if you have it installed)
   npx http-server -p 3000
   ```

## API Endpoints

### Game Management
- `GET /api/game/test` - Health check
- `POST /api/game/create` - Create new game
- `GET /api/game/{gameId}` - Get game state
- `POST /api/game/{gameId}/move` - Make a move

### Request/Response Examples

**Create Game:**
```bash
POST /api/game/create
Response: GameState object with gameId
```

**Make Move:**
```bash
POST /api/game/{gameId}/move
Body: {
  "row": 0,
  "col": 1,
  "size": "MEDIUM",
  "playerColor": "RED"
}
```

## Game Features

### Gameplay Features
- **Turn-based gameplay** with visual indicators
- **Ring selection system** with preview
- **Move validation** and illegal move prevention
- **Multiple win condition detection**
- **Move history tracking**
- **Turn timer** for competitive play

### Visual Features
- **Responsive design** for desktop and mobile
- **Smooth animations** for ring placement
- **Particle effects** for enhanced feedback
- **Color-coded players** with distinct ring colors
- **Winning line highlighting**

### Interactive Features
- **Keyboard shortcuts** (1/2/3 for ring sizes, N for new game)
- **Sound toggle** (visual feedback simulation)
- **Hover effects** and visual feedback
- **Mobile-responsive controls**

## Development

### Backend Development
The backend uses a clean architecture with:
- **Controllers** for API endpoints
- **Services** for business logic
- **Models** for data structures
- **Configuration** for CORS and web setup

### Frontend Development
The frontend is a single-page application with:
- **Modular JavaScript** functions
- **CSS Grid** layout system
- **Event-driven architecture**
- **State management** in JavaScript

## Configuration

### CORS Configuration
The backend is configured to allow cross-origin requests from any origin for development. For production, modify `WebConfig.java` to restrict origins.

### Game Configuration
Game constants can be modified in `GameState.java`:
- `BOARD_SIZE` - Grid size (default: 3x3)
- `COLORS` - Available player colors
- `SIZES` - Ring sizes available

## Browser Compatibility

- **Chrome** 90+
- **Firefox** 88+
- **Safari** 14+
- **Edge** 90+

## Known Issues

- The current implementation stores game state in memory (ConcurrentHashMap)
- No persistence layer - games are lost on server restart
- No WebSocket implementation for real-time updates (future enhancement)

## Future Enhancements

- [ ] Database persistence with JPA/Hibernate
- [ ] WebSocket real-time gameplay
- [ ] User authentication and profiles
- [ ] Game room management
- [ ] Spectator mode
- [ ] Tournament system
- [ ] Mobile app versions
- [ ] AI opponent with difficulty levels
- [ ] Support more than 2 players
- [ ] Support custom grids ie 4x4, 5x5, 6x6

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request


---

**Ring Wars** - Where strategy meets creativity in the ultimate tic-tac-toe evolution!