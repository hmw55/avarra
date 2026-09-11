import { Route, Routes } from 'react-router-dom'
import HomePage from './pages/HomePage'
import EnterPage from './pages/EnterPage'
import RegisterPage from './pages/RegisterPage'
import LoginPage from './pages/LoginPage'
import GameEntryPage from './pages/GameEntryPage'
import AboutPage from './pages/AboutPage'
import LorePage from './pages/LorePage'
import NewJourneyPage from './pages/NewJourneyPage'
import JourneyLorePage from './pages/JourneyLorePage'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/enter" element={<EnterPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/journeys" element={<GameEntryPage />} />
      <Route path="/about" element={<AboutPage />} />
      <Route path="/lore" element={<LorePage />} />
      <Route path="/journeys/new" element={<NewJourneyPage />} />
      <Route path="/journeys/new/lore" element={<JourneyLorePage />} />
    </Routes>
  )
}

export default App