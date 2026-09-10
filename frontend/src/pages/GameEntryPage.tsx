import { Link, useNavigate } from 'react-router-dom'
import { logout } from '../auth/authApi'
import { useAuth } from '../auth/useAuth'

function GameEntryPage() {
  const { user, setUser } = useAuth()
  const navigate = useNavigate()

  async function handleLogout() {
    try {
      await logout()
      setUser(null)
      navigate('/enter')
    } catch {
      // Proper logout error handling will be added with the UI pass.
    }
  }

  return (
    <div className="app">
      <header className="site-header">
        <Link to="/" className="site-title">
          AVARRA
        </Link>

        {user && (
          <button type="button" onClick={handleLogout}>
            Log Out
          </button>
        )}
      </header>

      <main className="home">
        <section className="intro-panel" aria-labelledby="game-entry-title">
          <p className="eyebrow">Your journey</p>

          <h1 id="game-entry-title">Avarra Awaits</h1>

          <p className="description">
            Choose how you would like to continue.
          </p>

          <div className="entry-actions">
            <button type="button" className="primary-action">
              Start New Game
            </button>

            <button type="button" className="secondary-action">
              Continue Game
            </button>
          </div>

          <Link to="/enter" className="back-link">
            Back
          </Link>
        </section>
      </main>

      <footer className="site-footer">
        <span>Avarra</span>
      </footer>
    </div>
  )
}

export default GameEntryPage