import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { register } from '../auth/authApi'

function RegisterPage() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [recoveryCode, setRecoveryCode] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    setError(null)
    setIsSubmitting(true)

    try {
      const result = await register({
        username,
        password,
        ...(email.trim() ? { email } : {}),
      })

      setRecoveryCode(result.recoveryCode)
    } catch {
      setError('Unable to create account.')
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
        <section className="intro-panel" aria-labelledby="register-title">
          <p className="eyebrow">Begin your journey</p>

          <h1 id="register-title">Create Account</h1>

          <p className="description">
            Create an account to preserve your journeys through Avarra.
          </p>

          {recoveryCode ? (
            <div>
              <h2>Save your recovery code</h2>

              <p>
                This code will only be shown once. Store it somewhere safe.
              </p>

              <code>{recoveryCode}</code>

              <Link to="/login" className="primary-action">
                Continue to Log In
              </Link>
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              <label>
                Username
                <input
                  type="text"
                  value={username}
                  onChange={(event) => setUsername(event.target.value)}
                  autoComplete="username"
                  required
                  minLength={3}
                  maxLength={32}
                />
              </label>

              <label>
                Email
                <input
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  autoComplete="email"
                />
              </label>

              <label>
                Password
                <input
                  type="password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  autoComplete="new-password"
                  required
                  minLength={12}
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
                {isSubmitting ? 'Creating...' : 'Create Account'}
              </button>
            </form>
          )}

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

export default RegisterPage