import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import EntryLayout from '../components/layout/EntryLayout'
import { register } from '../auth/authApi'

/**
 * Registers a new Avarra player account.
 *
 * After successful registration, the one-time recovery code is shown before
 * the player continues to login. Account creation and recovery-code generation
 * are handled by the backend.
 */
function RegisterPage() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [recoveryCode, setRecoveryCode] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  // Intentional console easter egg for new travelers.
  useEffect(() => {
    console.info(
      '%cMana. Mana. Mana. The world remembers, but we do not have password recovery. Store that recovery code somewhere safe.',
      'color: #c8ba96; font-weight: bold;',
    )
  }, [])

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
    <EntryLayout
      eyebrow="Begin your journey"
      title="Create Account"
      description="Create an account to preserve your journeys through Avarra."
      backTo="/enter"
      backLabel="Back"
    >
      {/* The recovery code is intentionally shown before leaving registration
          because the backend returns it only once. */}
      {recoveryCode ? (
        <div className="entry-recovery">
          <h2>Save Your Recovery Code</h2>

          <div className="entry-recovery-warning">
            <strong>THIS IS CURRENTLY THE ONLY WAY TO RECOVER YOUR ACCOUNT.</strong>

            <p>
              Avarra is a free game with ongoing infrastructure costs, so email-based
              password recovery was intentionally omitted from the initial release.
            </p>

            <p>
              This code is shown only once. Store it somewhere safe before continuing.
            </p>
          </div>

          <code>{recoveryCode}</code>

          <Link
            to="/login"
            className="entry-primary-action"
          >
            I Saved My Code — Continue to Log In
          </Link>
        </div>
      ) : (
        <>
          <div className="entry-info-box">
            <strong>Only a username and password are required.</strong>

            <p>
              Email is optional. Email-based account recovery is not currently available, 
              but may be added in the future. 
            </p>
          </div>

          <form className="entry-form" onSubmit={handleSubmit}>
            <label className="entry-field">
              <span>Username</span>

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

            <label className="entry-field">
              <span>Email</span>

              <input
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                autoComplete="email"
              />
            </label>

            <label className="entry-field">
              <span>Password</span>

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
              <p className="entry-form-error" role="alert">
                {error}
              </p>
            )}

            <button
              type="submit"
              className="entry-primary-action"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Creating...' : 'Create Account'}
            </button>
          </form>
        </>
      )}
    </EntryLayout>
  )
}

export default RegisterPage