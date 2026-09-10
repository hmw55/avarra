import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login } from '../auth/authApi'
import { useAuth } from '../auth/useAuth'

function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const { setUser } = useAuth();
  const navigate = useNavigate()

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    setError(null)
    setIsSubmitting(true)

    try {
      const result = await login({
        username,
        password,
      })

      setUser({
        username: result.username,
      })

      navigate('/play')
    } catch {
      setError('Unable to log in with those credentials.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="app">
      <header className="site-header">
        <Link to="/" className="site-title">
          AVARRA
        </Link>
      </header>

      <main className="home">
        <section className="intro-panel" aria-labelledby="login-title">
          <p className="eyebrow">Return to Avarra</p>

          <h1 id="login-title">Log In</h1>

          <p className="description">
            Continue a saved journey.
          </p>

          <form onSubmit={handleSubmit}>
            <label>
              Username
              <input
                type="text"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                autoComplete="username"
                required
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                autoComplete="current-password"
                required
              />
            </label>

            {error && (
              <p role="alert">
                {error}
              </p>
            )}

            <button
              type="submit"
              className="primary-action"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Entering...' : 'Log In'}
            </button>
          </form>

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

export default LoginPage