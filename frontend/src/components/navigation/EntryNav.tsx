import { Link, useNavigate } from 'react-router-dom'
import { logout } from '../../auth/authApi'
import { useAuth } from '../../auth/useAuth'
import './EntryNav.css'

/**
 * 
 * Shared navigation for Avarra's pre-game interface.
 * 
 * Provides access to general information and external project resources.
 * Authenticated users also receive a logout control. Gameplay will use its own
 * navigation rather than this component.
 */
function EntryNav() {
  const { user, setUser } = useAuth()
  const navigate = useNavigate()

  async function handleLogout() {
    try {
      await logout()
      setUser(null)
      navigate('/enter')
    } catch {
      // TODO: Surface logout failures to the user instead of failing silently.
    }
  }

  return (
    <header className="entry-nav">
      <Link to="/" className="entry-nav-brand">
        <span className="entry-nav-mark" aria-hidden="true">
          ♆
        </span>

        <span>AVARRA</span>
      </Link>

      <nav className="entry-nav-links" aria-label="Main navigation">
        <Link to="/about" className="entry-nav-link">
          About
        </Link>

        <Link to="/lore" className="entry-nav-link">
          Lore
        </Link>

        <a
          href="https://github.com/hmw55/avarra"
          className="entry-nav-link"
          target="_blank"
          rel="noreferrer"
        >
          GitHub
        </a>

        {user && (
          <button
            type="button"
            className="entry-nav-link"
            onClick={handleLogout}
          >
            Logout
          </button>
        )}
      </nav>
    </header>
  )
}

export default EntryNav