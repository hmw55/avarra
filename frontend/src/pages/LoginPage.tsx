import { useState, useEffect } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'
import { login } from '../auth/authApi'
import { useAuth } from '../auth/useAuth'

/**
 * Authenticates a returning player and restores access to their journeys.
 *
 * Successful authentication updates the shared frontend auth state before
 * routing the player to journey selection. Session creation and credential
 * validation are handled by the backend.
 */
function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const { setUser } = useAuth()
  const navigate = useNavigate()

  // Intentional console easter egg for returning travelers.
  useEffect(() => {
    console.info(
      '%cAvarra remembers you. Your password, thankfully, it does not.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

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

      navigate('/journeys')
    } catch {
      setError('Unable to log in with those credentials.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <EntryLayout
      eyebrow="Return to Avarra"
      title="Log In"
      description="Continue a saved journey."
      backTo="/enter"
      backLabel="Back"
    >
      <form className="entry-form" onSubmit={handleSubmit}>
        <label className="entry-field">
          <span>Username</span>

          <input
            type="text"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            autoComplete="username"
            required
          />
        </label>

        <label className="entry-field">
          <span>Password</span>

          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            autoComplete="current-password"
            required
          />
        </label>

        {error && (
          <p className="entry-form-error" role="alert">
            {error}
          </p>
        )}

        <button
          type="submit"
          className="entry-primary-action"
          disabled={isSubmitting}
        >
          {isSubmitting ? 'Entering...' : 'Log In'}
        </button>
      </form>
    </EntryLayout>
  )
}

export default LoginPage