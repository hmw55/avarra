import { Route, Routes } from 'react-router-dom'
import HomePage from './pages/HomePage'
import EnterPage from './pages/EnterPage'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/enter" element={<EnterPage />} />
    </Routes>
  )
}

export default App