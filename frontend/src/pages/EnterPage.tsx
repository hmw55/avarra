import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/useAuth'

function EnterPage() {
  const navigate = useNavigate()
  const { continueAsGuest } = useAuth()

  function handleContinueAsGuest() {
    continueAsGuest()
    navigate('/play')
  }

  return (
    <div className="app">
      <header className="site-header">
        <Link to="/" className="site-title">
          AVARRA
        </Link>
      </header>

      <main className="home">
        <section className="intro-panel" aria-labelledby="entry-title">
          <p className="eyebrow">Enter the world</p>

          <h1 id="entry-title">Enter Avarra</h1>

          <p className="description">
            Choose how you would like to continue.
          </p>

          <div className="entry-actions">
            <Link to="/login" className="primary-action">
              Log In
            </Link>

            <Link to="/register" className="secondary-action">
              Create Account
            </Link>

            <button
              type="button"
              className="secondary-action"
              onClick={handleContinueAsGuest}
            >
              Continue as Guest
            </button>
          </div>

          <Link to="/" className="back-link">
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

export default EnterPage